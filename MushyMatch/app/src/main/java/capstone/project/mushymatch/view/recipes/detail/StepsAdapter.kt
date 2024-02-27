package capstone.project.mushymatch.view.recipes.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import capstone.project.mushymatch.databinding.ItemStepsBinding

class StepsAdapter(private val stepsList: List<String>) : RecyclerView.Adapter<StepsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemStepsBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val step = stepsList[position]
        holder.bind(step)
    }

    override fun getItemCount(): Int {
        return stepsList.size
    }

    class ViewHolder(private val binding: ItemStepsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(step: String) {
            binding.tvSteps.text = step
        }
    }
}
