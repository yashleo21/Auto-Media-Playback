package holofyassignment.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.google.android.exoplayer2.ExoPlayer
import com.yash2108.holofyassignment.R
import com.yash2108.holofyassignment.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import holofyassignment.ui.fragment.DetailFragment
import holofyassignment.ui.fragment.HomeFragment
import holofyassignment.viewmodels.HomeViewModel
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val TAG = HomeActivity::class.java.simpleName

    private val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var exoPlayer: ExoPlayer

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBackStackListener()
        inflateFragment(savedInstanceState = savedInstanceState)
        Log.d(TAG, "${viewModel.fetchData()}")
    }

    private fun inflateFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<HomeFragment>(R.id.fragment_containing_view)
            }
        }
    }

    private fun initBackStackListener() {
        supportFragmentManager?.addOnBackStackChangedListener(object :
            FragmentManager.OnBackStackChangedListener {
            override fun onBackStackChanged() {
                when (val fragment =
                    supportFragmentManager?.findFragmentById(R.id.fragment_containing_view)) {
                    is HomeFragment -> {
                        Log.d(TAG, "Home fragment on top")
                        //     fragment.resumePlayer()
                    }

                    is DetailFragment -> {
                        Log.d(TAG, "Detail fragment on top")
                    }
                }
            }
        })
    }
}