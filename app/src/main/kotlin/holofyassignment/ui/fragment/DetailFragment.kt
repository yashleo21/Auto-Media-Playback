package holofyassignment.ui.fragment

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.exoplayer2.ExoPlayer
import com.yash2108.holofyassignment.R
import com.yash2108.holofyassignment.databinding.FragmentDetailBinding
import com.yash2108.holofyassignment.databinding.FragmentDetailNewBinding
import dagger.hilt.android.AndroidEntryPoint
import holofyassignment.viewmodels.HomeViewModel
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val TAG = DetailFragment::class.java.simpleName
    @Inject
    lateinit var exoplayer: ExoPlayer

    private var _binding: FragmentDetailNewBinding? = null
    private val binding: FragmentDetailNewBinding get() = _binding!!

    private val viewModel by activityViewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailNewBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        assignTransitionName()
        initUI()
        bindAndConfigurePlayer()
    }

    private fun assignTransitionName() {
        binding.root.transitionName = viewModel.transitionName
    }

    private fun initUI() {
        binding.tvTitle.text = viewModel.title
        binding.tvSubtitle.text = viewModel.subtitle
    }

    private fun bindAndConfigurePlayer() {
        exoplayer.audioComponent?.volume = 100f
        binding.player.player = exoplayer
    }

    private fun pausePlayer() {
        exoplayer.playWhenReady = false
    }

    private fun resumePlayer() {
        exoplayer.playWhenReady = true
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onResume() {
        super.onResume()
        resumePlayer()
    }

    override fun onDestroyView() {
        binding.player.player = null
        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance(): DetailFragment {
            val fragment = DetailFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}