package holofyassignment.exoplayer

import android.content.Context
import android.util.Log
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.upstream.cache.*
import com.google.android.exoplayer2.util.Util
import holofyassignment.support.Utils
import java.io.EOFException
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ExoplayerCache @Inject constructor() {

    private val TAG = ExoplayerCache::class.java.simpleName

    fun cacheVideo(
        dataSpec: DataSpec,
        cache: SimpleCache,
        progressListener: CacheWriter.ProgressListener?
    ) {
        try {
            // Create a data source factory.
            val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()

            val cacheDataSourceFactory: CacheDataSource.Factory = CacheDataSource.Factory()
                .setCache(cache)
                .setUpstreamDataSourceFactory(dataSourceFactory)

            cacheDataSourceFactory.createDataSource()

            val cacheWriter = CacheWriter(
                Utils.buildCacheDataSource(cache),
                dataSpec,
                null,
                progressListener
            )


            cacheWriter.cache()
            Log.d(TAG, "Cache request sent for ${dataSpec}")
        } catch (e: EOFException) {
            Log.d(TAG, "EOF Exception: ${e.printStackTrace()}")
        } catch (e: IOException) {
            Log.d(TAG, "IO EXception: ${e.printStackTrace()}")
        } catch (e: Exception) {
            Log.d(TAG, "General exception: ${e.printStackTrace()}")
        }

    }
}