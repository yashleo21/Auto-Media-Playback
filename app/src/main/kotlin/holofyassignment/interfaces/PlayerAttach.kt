package holofyassignment.interfaces

import com.google.android.exoplayer2.ExoPlayer

interface PlayerAttach {
    fun attachExoplayer(exoPlayer: ExoPlayer)
    fun exoplayerPlaybackState(state: Int, exoPlayer: ExoPlayer)
}