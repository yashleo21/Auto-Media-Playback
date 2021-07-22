package holofyassignment.viewmodels
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import dagger.hilt.android.lifecycle.HiltViewModel
import holofyassignment.exoplayer.ExoplayerCache
import holofyassignment.models.HomeDataObject
import holofyassignment.repositories.HomeRepository
import holofyassignment.support.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository,
                                        private val cache: SimpleCache,
                                        private val exoplayerCache: ExoplayerCache): ViewModel() {


    var homeDataMutableLiveData: MutableLiveData<ArrayList<HomeDataObject>> = MutableLiveData()
    val homeDataLiveData: LiveData<ArrayList<HomeDataObject>> get() = homeDataMutableLiveData

    //For Playback
    var activeViewPosition = -1
    //Exoplayer active view visibility threshold. Currently set to 25%
    val viewVisibilityThresholdEnter = 25f
    val viewVisibilityThreshholdExit = 45f

    var data: ArrayList<HomeDataObject> = ArrayList()

    fun fetchData() = viewModelScope.launch {
        data = repository.getHomeData()
        homeDataMutableLiveData.postValue(data)
    }

    fun cacheVideos() = viewModelScope.launch (Dispatchers.IO) {
        data.map { it.sources }?.forEach { source ->
            val uri = source?.elementAtOrNull(0) ?: ""
            if (uri.isNotBlank()) {
                exoplayerCache.cacheVideo(DataSpec(Uri.parse(uri)), cache, null)
            }
        }
    }


    /**
     * For Detail Fragment
     */

    var title: String = ""
    var subtitle: String = ""
    var transitionName: String = ""
}