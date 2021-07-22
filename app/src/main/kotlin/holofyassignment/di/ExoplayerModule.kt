package holofyassignment.di

import android.app.Application
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import holofyassignment.exoplayer.ExoplayerCache
import holofyassignment.support.Utils
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExoplayerModule {

    @Singleton
    @Provides
    fun providesExoplayerInstance(application: Application, cache: SimpleCache): ExoPlayer {
        val loadControlBuilder = DefaultLoadControl.Builder()
        val MIN_BUFFER_DURATION = 2000
        val MAX_BUFFER_DURATION = 5000
        val MIN_PLAYBACK_START_BUFFER = 1024
        val MIN_PLAYBACK_RESUME_BUFFER = 1024
        loadControlBuilder.setBufferDurationsMs(
            MIN_BUFFER_DURATION,
            MAX_BUFFER_DURATION,
            MIN_PLAYBACK_START_BUFFER,
            MIN_PLAYBACK_RESUME_BUFFER
        )

        val player = SimpleExoPlayer.Builder(application)
            .setLoadControl(loadControlBuilder.build())
            .setMediaSourceFactory(DefaultMediaSourceFactory(Utils.buildCacheDataSourceFactory(cache)))
            .build()

        player.repeatMode = Player.REPEAT_MODE_ALL
        player.audioComponent?.volume = 0f
        return player
    }

    @Singleton
    @Provides
    fun providesSimpleCache(application: Application): SimpleCache {
        val exoplayerCacheSize: Long = 90 * 1024 * 1024L
        val leastRecentlyUsedCacheEvictor = LeastRecentlyUsedCacheEvictor(exoplayerCacheSize)
        val exoDatabaseProvider = ExoDatabaseProvider(application)

        return SimpleCache(
            File(application.cacheDir, "media"),
            leastRecentlyUsedCacheEvictor,
            exoDatabaseProvider
        )
    }
}