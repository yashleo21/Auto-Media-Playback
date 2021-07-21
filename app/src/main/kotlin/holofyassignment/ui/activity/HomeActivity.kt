package holofyassignment.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.yash2108.holofyassignment.R
import dagger.hilt.android.AndroidEntryPoint
import holofyassignment.viewmodels.HomeViewModel

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val TAG = HomeActivity::class.java.simpleName

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "${viewModel.getHomeDataList()}")
    }
}