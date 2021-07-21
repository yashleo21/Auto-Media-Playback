package holofyassignment.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.yash2108.holofyassignment.R
import com.yash2108.holofyassignment.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import holofyassignment.ui.fragment.HomeFragment
import holofyassignment.viewmodels.HomeViewModel

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val TAG = HomeActivity::class.java.simpleName

    private val viewModel: HomeViewModel by viewModels()

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inflateFragment(savedInstanceState = savedInstanceState)
        Log.d(TAG, "${viewModel.getHomeDataList()}")
    }

    private fun inflateFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<HomeFragment>(R.id.fragment_containing_view)
            }
        }
    }
}