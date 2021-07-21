package holofyassignment.viewmodels
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import holofyassignment.repositories.HomeRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository): ViewModel() {

    fun getHomeDataList() = repository.getHomeData()
}