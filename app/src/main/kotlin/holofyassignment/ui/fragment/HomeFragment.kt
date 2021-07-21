package holofyassignment.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.yash2108.holofyassignment.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import holofyassignment.adapters.HomeAdapter
import holofyassignment.models.HomeDataObject
import holofyassignment.viewmodels.HomeViewModel
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment: Fragment(), HomeAdapter.Callback {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<HomeViewModel>()
    @Inject lateinit var adapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindAdapter()
        prepareAndUpdateAdapter()
    }

    private fun bindAdapter() {
        binding.rvItems.adapter = adapter
    }

    private fun prepareAndUpdateAdapter() {
        viewModel?.getHomeDataList()?.let {
            adapter.submitList(it.toList())
        }
    }

    override fun onItemClicked(data: HomeDataObject, position: Int) {

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance(): HomeFragment {
            val fragment = HomeFragment()
            return fragment
        }
    }
}
