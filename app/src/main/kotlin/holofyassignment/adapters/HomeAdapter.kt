package holofyassignment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yash2108.holofyassignment.databinding.ItemListHomeBinding
import holofyassignment.models.HomeDataObject
import javax.inject.Inject

class HomeAdapter @Inject constructor(val callback: Callback): ListAdapter<HomeDataObject, HomeAdapter.ItemViewHolder>(HomeDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ItemListHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindView(getItem(position))
    }

    inner class ItemViewHolder(val binding: ItemListHomeBinding): RecyclerView.ViewHolder(binding.root) {

        fun bindView(data: HomeDataObject) {
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
        fun onItemClicked(data: HomeDataObject, position: Int)
    }
}