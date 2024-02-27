package capstone.project.mushymatch.view.recipes.detail

import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.media3.session.MediaController
import androidx.recyclerview.widget.LinearLayoutManager
import capstone.project.mushymatch.R
import capstone.project.mushymatch.api.ApiConfig
import capstone.project.mushymatch.api.repository.MushroomRepository
import capstone.project.mushymatch.databinding.ActivityCookingRecipesBinding
import capstone.project.mushymatch.view.recipes.list.RecipeAdapter
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class CookingRecipesActivity : AppCompatActivity() {
    private lateinit var viewModel: CookingRecipesViewModel
    private lateinit var binding: ActivityCookingRecipesBinding
    private lateinit var ingredientsAdapter: IngredientsAdapter
    private lateinit var stepsAdapter: StepsAdapter
    private lateinit var player: SimpleExoPlayer
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private val coroutineScope = CoroutineScope(Dispatchers.Main)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCookingRecipesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            supportActionBar?.title = "Cooking Recipe"
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.back_to) // Ganti dengan gambar ikon kembali yang diinginkan
        }

        //inisialisasi player
        player = SimpleExoPlayer.Builder(this).build()

        binding.rvIngredients.layoutManager = LinearLayoutManager(this) // LinearLayoutManager menjadi GridLayoutManager
        binding.rvSteps.layoutManager = LinearLayoutManager(this) // LinearLayoutManager menjadi GridLayoutManager
        binding.rvIngredients.isNestedScrollingEnabled = false // Menonaktifkan nested scrolling pada RecyclerView
        binding.rvSteps.isNestedScrollingEnabled = false // Menonaktifkan nested scrolling pada RecyclerView


        val repository = MushroomRepository(ApiConfig.createApiService())
        val viewModelFactory = CookingRecipesViewModelFactory(repository) // Buat ViewModelFactory
        viewModel = ViewModelProvider(this, viewModelFactory).get(CookingRecipesViewModel::class.java)

        val recipeId = intent.getIntExtra("recipe_id", -1) // Menggunakan key "recipe_id"
        if (recipeId != -1) {
            viewModel.loadRecipeDetail(recipeId)
        }

        showVideo(false)
        binding.btnVideo.setOnClickListener {
            showVideo(true)
        }
        shimmerFrameLayout = binding.shimmerViewContainer
        startShimmer()

        coroutineScope.launch {
            delay(1000)
            viewModel.recipeDetail.observe(this@CookingRecipesActivity) { recipeDetail ->
                binding.let {
                    it.tvRecipesName.text = recipeDetail.nameRecipe
                    Glide.with(this@CookingRecipesActivity)
                        .load(recipeDetail.pictRecipe)
                        .into(it.ivRecipesImage)

                    // Inisialisasi ExoPlayer
                    player = SimpleExoPlayer.Builder(this@CookingRecipesActivity).build()
                    binding.playerView.player = player
                    // Buat MediaItem dengan URL video
                    val videoUri = Uri.parse(recipeDetail.video)
                    val mediaItem = MediaItem.fromUri(videoUri)

                    // Set media item ke player dan play video
                    player.setMediaItem(mediaItem)
                    player.prepare()

                    val ingredientsList = recipeDetail.ingredients.split("\n").filter { it.isNotBlank() }
                    ingredientsAdapter = IngredientsAdapter(ingredientsList)
                    it.rvIngredients.adapter = ingredientsAdapter
                    Log.d("ingredients", ingredientsList.toString())

                    val stepsList = recipeDetail.steps.split("\n").filter { it.isNotBlank() }
                    stepsAdapter = StepsAdapter(stepsList)
                    it.rvSteps.adapter = stepsAdapter
                    Log.d("steps", stepsList.toString())

                    stopShimmer()
                }
            }

        }

    }

    fun startShimmer() {
        binding.shimmerViewContainer.startShimmer()
        binding.shimmerViewContainer.visibility= android.view.View.VISIBLE
        binding.content.visibility= android.view.View.GONE
    }

    fun stopShimmer() {
        binding.shimmerViewContainer.stopShimmer()
        binding.shimmerViewContainer.visibility= android.view.View.GONE
        binding.content.visibility= android.view.View.VISIBLE
    }

    fun showVideo(state: Boolean) {
        if (state) {
            binding.ivRecipesImage.visibility = android.view.View.GONE
            binding.playerView.visibility = android.view.View.VISIBLE
            player.play()
        } else {
            binding.ivRecipesImage.visibility = android.view.View.VISIBLE
            binding.playerView.visibility = android.view.View.GONE
        }
    }

    override fun onStop() {
        super.onStop()
        player.stop()
        player.release()
    }

    override fun onStart() {
        super.onStart()
        player.play()
    }


    override fun onPause() {
        super.onPause()
        player.pause()
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
}

