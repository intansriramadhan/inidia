package capstone.project.mushymatch.view.recipes.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import capstone.project.mushymatch.databinding.ItemIngredientsBinding

class IngredientsAdapter(private val ingredientsList: List<String>) : RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemIngredientsBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = ingredientsList[position]
        holder.bind(ingredient)
    }

    override fun getItemCount(): Int {
        return ingredientsList.size
    }

    class ViewHolder(private val binding: ItemIngredientsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ingredient: String) {
            binding.tvIngredientName.text = ingredient
        }
    }
}
