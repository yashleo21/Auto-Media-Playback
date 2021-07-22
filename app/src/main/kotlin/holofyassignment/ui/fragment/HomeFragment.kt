package holofyassignment.ui.fragment

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.yash2108.holofyassignment.R
import com.yash2108.holofyassignment.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import holofyassignment.adapters.HomeAdapter
import holofyassignment.interfaces.PlayerAttach
import holofyassignment.models.HomeDataObject
import holofyassignment.support.Utils.STATE_RESET_PLAYER
import holofyassignment.support.Utils.STATE_RESUME_PLAYER
import holofyassignment.viewmodels.HomeViewModel
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), HomeAdapter.Callback {

    private val TAG = HomeFragment::class.java.simpleName

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<HomeViewModel>()

    @Inject
    lateinit var adapter: HomeAdapter

    @Inject
    lateinit var exoplayer: ExoPlayer

    private var listener: Player.Listener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindAdapter()
        initListeners()
        initObservers()
        postponeEnterTransition()
        fetchData()
        cacheVideos()
    }

    private fun bindAdapter() {
        binding.rvItems.adapter = adapter
    }

    private fun initListeners() {
        binding.rvItems.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val globalRect = Rect()
                recyclerView.getGlobalVisibleRect(globalRect)
                determineActiveHolder(recyclerView, globalRect)
            }
        })

        listener = object : Player.Listener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)
                Log.d(TAG, "Player state: $playbackState")
                val currentViewHolder = binding.rvItems.findViewHolderForAdapterPosition(
                    viewModel.activeViewPosition
                        ?: 0
                )
                if (currentViewHolder != null && currentViewHolder is PlayerAttach) {
                    currentViewHolder.exoplayerPlaybackState(playbackState, exoplayer)
                }
            }
        }
    }

    private fun initObservers() {
        viewModel.homeDataLiveData.observe(viewLifecycleOwner, Observer {
            prepareDataAndUpdateAdapter(data = it)
        })
    }

    private fun fetchData() {
        viewModel.fetchData()
    }

    private fun prepareDataAndUpdateAdapter(data: ArrayList<HomeDataObject>) {
        adapter.submitList(data.toList())

        (binding.root.parent as? ViewGroup)?.doOnPreDraw { startPostponedEnterTransition() }
    }

    /**
     * Logic to calculate the active view and pass the exoplayer instance to active view holder
     * - Initially check if the active holder's visibility is more than threshold, if it is, no longer need to continue calculation
     * - Else, find all viewport and according to the size, hand over the exoplayer and active status to that view holder
     */
    private fun determineActiveHolder(recyclerView: RecyclerView, globalVisibleRect: Rect) {
        val firstPosition =
            (recyclerView.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition()
        val lastPosition =
            (recyclerView.layoutManager as? LinearLayoutManager)?.findLastVisibleItemPosition()

        viewModel.activeViewPosition.let { activePosition ->
            if (activePosition != -1) {
                recyclerView.findViewHolderForAdapterPosition(activePosition)
                    ?.let { viewHolder ->
                        if (viewHolder is PlayerAttach) {
                            val visibilityPercent = getVisibilityPercentage(
                                globalVisibleRect = globalVisibleRect,
                                viewholder = viewHolder
                            )
                            viewModel.viewVisibilityThreshholdExit.let { visibilityThreshold ->
                                if (visibilityPercent >= visibilityThreshold) {
                                    Log.d(
                                        TAG,
                                        "Active view is media and above threshold ${visibilityPercent} no need to re-calculate"
                                    )
                                    return
                                } else {
                                    Log.d(
                                        TAG,
                                        "Active view is below threshold ${visibilityPercent}"
                                    )
                                    //Muting the player
                                    exoplayer.audioComponent?.volume = 0f
                                    (viewHolder as PlayerAttach).exoplayerPlaybackState(
                                        STATE_RESET_PLAYER,
                                        exoplayer
                                    )
                                    viewModel.activeViewPosition = -1
                                }
                            }
                        }
                    }
            }
        }

        val exoHolderPair = mutableListOf<Pair<Int, Float>>()
        for (pos in (firstPosition ?: 0)..(lastPosition ?: 0)) {
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(pos)
            if (viewHolder != null && viewHolder.itemView.height > 0) {
                //Find percentage of the views height w.r.t avaialble global rect height
                exoHolderPair.add(
                    Pair<Int, Float>(
                        pos,
                        getVisibilityPercentage(globalVisibleRect, viewHolder)
                    )
                )
            }
        }

        if (exoHolderPair.size > 0) {
            //
            if (exoHolderPair.size == 1) {
                if (viewModel?.activeViewPosition ?: 0 != exoHolderPair[0].first) {
                    val viewHolder =
                        recyclerView.findViewHolderForAdapterPosition(exoHolderPair[0].first)
                    if (viewHolder != null && viewHolder is PlayerAttach) {
                        Log.d(TAG, "Marking view holder as active for media playback")
                        viewModel?.activeViewPosition = exoHolderPair[0].first
                        (viewHolder as PlayerAttach)
                            .attachExoplayer(exoplayer)
                    }
                }

            } else {
                exoHolderPair.sortByDescending { it.second }
                for (pair in exoHolderPair) {
                    Log.d(TAG, "Sorted pair: POS: ${pair.first} PERCENTAGE: ${pair.second}")
                    val viewHolder = recyclerView.findViewHolderForAdapterPosition(pair.first)
                    if (viewHolder is PlayerAttach && pair.second >= viewModel?.viewVisibilityThresholdEnter!!) {
                        Log.d(TAG, "Marking view holder as active for media playback")
                        viewModel?.activeViewPosition = pair.first
                        (viewHolder as PlayerAttach)
                            .attachExoplayer(exoplayer)
                        break
                    }
                }
            }
        }
    }

    private fun getVisibilityPercentage(
        globalVisibleRect: Rect,
        viewholder: RecyclerView.ViewHolder
    ): Float {
        val itemVisibleRect = Rect()
        viewholder.itemView.getGlobalVisibleRect(itemVisibleRect)

        val visibilityExtent = if (itemVisibleRect.bottom >= globalVisibleRect.bottom) {
            val visibleHeight = globalVisibleRect.bottom - itemVisibleRect.top
            Log.d(
                TAG,
                "if  visibleHeight: $visibleHeight globalHeight: ${globalVisibleRect.height()}"
            )
            visibleHeight.toFloat()
        } else {
            val visibleHeight = itemVisibleRect.bottom - itemVisibleRect.top
            Log.d(
                TAG,
                "ELSE visibleHeight: $visibleHeight globalHeight: ${globalVisibleRect.height()}"
            )
            visibleHeight.toFloat()
        }

        return (visibilityExtent / globalVisibleRect.height()) * 100
    }

    private fun cacheVideos() {
        viewModel.cacheVideos()
    }

    private fun pausePlayer() {
        if (viewModel.activeViewPosition ?: 0 == -1) return
        exoplayer.playWhenReady = false
    }

    fun resumePlayer() {
        binding.rvItems.post {
            if (viewModel.activeViewPosition ?: 0 == -1) return@post
            val viewHolder = binding.rvItems.findViewHolderForAdapterPosition(
                viewModel.activeViewPosition
                    ?: 0
            )
            if (viewHolder != null && viewHolder is PlayerAttach) {
                viewHolder.exoplayerPlaybackState(STATE_RESUME_PLAYER, exoplayer)
            }
        }
    }

    override fun onItemClicked(data: HomeDataObject, position: Int, transitionView: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            //Sharing the same viewmodel, we can use VM to share data between the 2 fragments
            viewModel.title = data.title ?: ""
            viewModel.subtitle = data.subtitle ?: ""
            viewModel.transitionName = transitionView.transitionName

            val fragment = DetailFragment.newInstance()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                sharedElementReturnTransition = TransitionInflater.from(requireContext())
                    .inflateTransition(R.transition.default_transition)
                exitTransition = TransitionInflater.from(requireContext())
                    .inflateTransition(android.R.transition.no_transition)

                fragment.sharedElementEnterTransition = TransitionInflater.from(requireContext())
                    .inflateTransition(R.transition.default_transition)
                fragment.enterTransition = TransitionInflater.from(requireContext())
                    .inflateTransition(android.R.transition.no_transition)
            }

            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.fragment_containing_view, fragment, "detailFragment")
                ?.addSharedElement(transitionView, transitionView.transitionName)
                ?.addToBackStack("detail")
                ?.setReorderingAllowed(true)
                ?.commit()
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "On pause called")
        listener?.let {
            exoplayer.removeListener(it)
        }
        pausePlayer()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "On resume called")
        listener?.let {
            exoplayer.removeListener(it)
            exoplayer.addListener(it)
        }
        resumePlayer()
    }

    override fun onDestroyView() {
        _binding = null
        viewModel.homeDataLiveData.removeObservers(this)
        viewModel.homeDataMutableLiveData = MutableLiveData()
        super.onDestroyView()
    }
}
