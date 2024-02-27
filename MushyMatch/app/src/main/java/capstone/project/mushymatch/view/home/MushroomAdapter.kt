package capstone.project.mushymatch.view.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import capstone.project.mushymatch.api.response.mushroom.GetMushroomResponseItem
import capstone.project.mushymatch.databinding.ItemMushroomBinding
import com.bumptech.glide.Glide

class MushroomAdapter : RecyclerView.Adapter<MushroomAdapter.MushroomViewHolder>() {

    private val mushrooms: MutableList<GetMushroomResponseItem> = mutableListOf()
    private var clickListener: OnMushroomClickListener? = null

    fun setOnMushroomClickListener(listener: OnMushroomClickListener) {
        clickListener = listener
    }


    fun setMushrooms(newMushrooms: List<GetMushroomResponseItem>) {
        mushrooms.clear()
        mushrooms.addAll(newMushrooms)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MushroomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMushroomBinding.inflate(inflater, parent, false)
        return MushroomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MushroomViewHolder, position: Int) {
        val mushroom = mushrooms[position]
        holder.bind(mushroom)
    }

    override fun getItemCount(): Int {
        return mushrooms.size
    }

    interface OnMushroomClickListener {
        fun onMushroomClick(mushroom: GetMushroomResponseItem)
    }


    inner class MushroomViewHolder(private val binding: ItemMushroomBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(mushroom: GetMushroomResponseItem) {
            binding.tvMushroomName.text = mushroom.name
            binding.tvMushroomScientificName.text = mushroom.latinName
            Glide.with(binding.root)
                .load(mushroom.pict)
                .into(binding.ivMushroom)

            itemView.setOnClickListener {
                clickListener?.onMushroomClick(mushroom)
            }

        }

    }
}

