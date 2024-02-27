package capstone.project.mushymatch.view.recipes.list

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import capstone.project.mushymatch.api.response.recipe.ListRecipesResponseItem
import capstone.project.mushymatch.databinding.ItemRecipesBinding
import capstone.project.mushymatch.view.home.MushroomAdapter
import com.bumptech.glide.Glide

class RecipeAdapter : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    private val recipes: MutableList<ListRecipesResponseItem> = mutableListOf()
    private var clickListener: OnRecipeClickListener? = null

    fun setOnRecipeClickListener(listener: OnRecipeClickListener) {
        clickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setRecipes(newRecipes: List<ListRecipesResponseItem>) {
        recipes.clear()
        recipes.addAll(newRecipes)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecipesBinding.inflate(inflater, parent, false)
        return RecipeViewHolder(binding)

    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    interface OnRecipeClickListener {
        fun onRecipeClick(recipe: ListRecipesResponseItem)
    }
    inner class RecipeViewHolder(private val binding: ItemRecipesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: ListRecipesResponseItem) {
            binding.tvRecipesName.text = recipe.nameRecipe
            binding.tvMushroomName.text = recipe.nameJamur
            Log.d("RecipeAdapter", "bind: ${recipe.nameRecipe}")
            Glide.with(binding.root)
                .load(recipe.pictRecipe)
                .into(binding.ivRecipe)
            Log.d("RecipeAdapter", "bind: ${recipe.pictRecipe}")

            itemView.setOnClickListener {
                clickListener?.onRecipeClick(recipe)
            }
        }

    }
}