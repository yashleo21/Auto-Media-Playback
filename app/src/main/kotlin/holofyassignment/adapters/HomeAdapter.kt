package holofyassignment.adapters

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.yash2108.holofyassignment.databinding.ItemListHomeBinding
import holofyassignment.interfaces.PlayerAttach
import holofyassignment.models.HomeDataObject
import holofyassignment.support.Utils
import holofyassignment.support.Utils.STATE_RESET_PLAYER
import holofyassignment.support.Utils.STATE_RESUME_PLAYER
import javax.inject.Inject

class HomeAdapter @Inject constructor(val callback: Callback,
                                      val cache: SimpleCache): ListAdapter<HomeDataObject, HomeAdapter.ItemViewHolder>(HomeDiffUtil()) {

    private val TAG = HomeAdapter::class.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ItemListHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindView(getItem(position), position)
    }

    inner class ItemViewHolder(val binding: ItemListHomeBinding): RecyclerView.ViewHolder(binding.root), PlayerAttach {

        override fun attachExoplayer(exoPlayer: ExoPlayer) {
            Log.d(TAG, "Attach player called")
            //Clear previous instance
            binding.player.player = null
            binding.player.player = exoPlayer

            (binding.player.player as ExoPlayer).videoComponent?.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
            if (adapterPosition < 0) return

            mediaSource?.let {
                exoPlayer.setMediaSource(it)
                exoPlayer.playWhenReady = true
                exoPlayer.prepare()
            }
        }

        override fun exoplayerPlaybackState(state: Int, exoPlayer: ExoPlayer) {
            when (state) {
                Player.STATE_BUFFERING -> {
                    mediaSource?.let {
                        pauseState()
                    }

                }

                Player.STATE_READY -> {
                    mediaSource?.let {
                        resumeState()
                    }
                }

                STATE_RESET_PLAYER -> {
                    mediaSource?.let {
                        resetState()
                    }
                }

                STATE_RESUME_PLAYER -> {
                    mediaSource?.let {
                        resumePlayer(exoPlayer)
                    }
                }
            }
        }

        var mediaSource: MediaSource? = null

        init {
            attachListeners()
        }

        private fun attachListeners() {
            binding.root.setOnClickListener {
                if (adapterPosition < 0) return@setOnClickListener
                val position = adapterPosition

                callback.onItemClicked(getItem(position), position, binding.player)
            }
        }


        fun bindView(data: HomeDataObject, position: Int) {
            //Set uniuqe transition name
            binding.player.transitionName = "player$position"
            //Set title
            if (data.title?.isNotBlank() == true) {
                binding.tvTitle.visibility = View.VISIBLE
                binding.tvTitle.text = data.title
            } else {
                binding.tvTitle.visibility = View.GONE
            }

            //Subtitle
            if (data.subtitle?.isNotBlank() == true) {
                binding.tvSubtitle.visibility = View.VISIBLE
                binding.tvSubtitle.text = data.subtitle
            } else {
                binding.tvSubtitle.visibility = View.GONE
            }

            //Prepare media source
            mediaSource = if (data.sources?.elementAtOrNull(0)?.isNotBlank() == true) Utils.buildMediaSource(url = data.sources?.elementAtOrNull(0) ?: "",
                cache = cache) else null
        }

        private fun pauseState() {
            binding.thumbnail.visibility = View.VISIBLE
            binding.player.visibility = View.INVISIBLE
        }

        private fun resumeState() {
            binding.player.visibility = View.VISIBLE
            binding.thumbnail.visibility = View.INVISIBLE
        }

        private fun resetState() {
            if (binding.player.player != null) {
                (binding.player.player as ExoPlayer).playWhenReady = false
                binding.thumbnail.visibility = View.VISIBLE
                binding.player.visibility = View.INVISIBLE
            }
        }

        fun resumePlayer(player: ExoPlayer) {
            if (binding.player.player == null) {
                binding.player.player = player
            }
            binding.player.player?.playWhenReady = true
        }
    }

    class HomeDiffUtil(): DiffUtil.ItemCallback<HomeDataObject>() {
        override fun areItemsTheSame(oldItem: HomeDataObject, newItem: HomeDataObject): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: HomeDataObject, newItem: HomeDataObject): Boolean {
            return oldItem == newItem
        }
    }

    interface Callback {
        fun onItemClicked(data: HomeDataObject, position: Int, transitionView: View)
    }
}