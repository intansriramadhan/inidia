package capstone.project.mushymatch.view.recipes.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import capstone.project.mushymatch.R
import capstone.project.mushymatch.api.ApiConfig
import capstone.project.mushymatch.api.repository.MushroomRepository
import capstone.project.mushymatch.api.response.recipe.ListRecipesResponseItem
import capstone.project.mushymatch.databinding.ActivityListRecipesBinding
import capstone.project.mushymatch.view.recipes.detail.CookingRecipesActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class ListRecipesActivity : AppCompatActivity(), RecipeAdapter.OnRecipeClickListener {

    private lateinit var binding: ActivityListRecipesBinding
    private lateinit var viewModel: RecipeViewModel
    private lateinit var adapter: RecipeAdapter
    private val coroutineScope = CoroutineScope(Dispatchers.Main)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListRecipesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            supportActionBar?.title = "List Cooking Recipes"
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.back_to) // Ganti dengan gambar ikon kembali yang diinginkan
        }

        // Inisialisasi ViewModel
        val repository = MushroomRepository(ApiConfig.createApiService())
        viewModel = ViewModelProvider(
            this,
            RecipeViewModelFactory(repository)
        )[RecipeViewModel::class.java]

        // Inisialisasi RecyclerView
        adapter = RecipeAdapter()
        binding.rvRecipes.layoutManager = LinearLayoutManager(this)
        binding.rvRecipes.adapter = adapter
        adapter.setOnRecipeClickListener(this)

        startShimmer()
        // Amati perubahan pada LiveData recipes
        coroutineScope.launch {
            delay(1000)
            viewModel.recipes.observe(this@ListRecipesActivity) { recipes ->
                adapter.setRecipes(recipes)
                Log.d("ListRecipesActivity", "recipes: $recipes")
                stopShimmer()
            }
        }


        //get id mushroom
        val idMushroom = intent.getIntExtra("idJamur", 1)

        Log.d("ListRecipesActivity", "idJamur: $idMushroom")

        // Memuat data resep dari repository
        viewModel.loadRecipes(idMushroom) // Ganti dengan ID jamur yang sesuai


    }

    //start shimmer
    private fun startShimmer() {
        binding.shimmerViewContainer.startShimmer()
        binding.rvRecipes.visibility = android.view.View.GONE
    }

    //stop shimmer
    private fun stopShimmer() {
        binding.shimmerViewContainer.stopShimmer()
        binding.shimmerViewContainer.visibility = android.view.View.GONE
        binding.rvRecipes.visibility = android.view.View.VISIBLE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // Atur aksi saat tombol kembali diklik
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRecipeClick(recipe: ListRecipesResponseItem) {
        val intent = Intent(this, CookingRecipesActivity::class.java)
        intent.putExtra("recipe_id", recipe.idRecipe) // Menggunakan key "recipe_id"
        startActivity(intent)
    }

}