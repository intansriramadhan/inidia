package capstone.project.mushymatch.view.recipes.list

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import capstone.project.mushymatch.api.repository.MushroomRepository
import capstone.project.mushymatch.api.repository.Result
import capstone.project.mushymatch.api.response.recipe.ListRecipesResponseItem
import kotlinx.coroutines.launch

class RecipeViewModel(private val repository: MushroomRepository) : ViewModel() {

    private val _recipes = MutableLiveData<List<ListRecipesResponseItem>>()
    val recipes = _recipes

    fun loadRecipes(idMushroom: Int) {
        viewModelScope.launch {
            when (val response = repository.getRecipes(idMushroom)) {
                is Result.Success -> _recipes.value = response.response!!
                is Result.Error -> Log.e("RecipeViewModel", response.message)
            }
        }
    }

}

class RecipeViewModelFactory(private val repository: MushroomRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}