package capstone.project.mushymatch.view.recipes.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import capstone.project.mushymatch.api.repository.MushroomRepository
import capstone.project.mushymatch.api.repository.Result
import capstone.project.mushymatch.api.response.recipe.DetailRecipesResponse
import kotlinx.coroutines.launch

class CookingRecipesViewModel(private val mushroomRepository: MushroomRepository) : ViewModel() {
    private val _recipeDetail = MutableLiveData<DetailRecipesResponse>()
    val recipeDetail: LiveData<DetailRecipesResponse> = _recipeDetail

    fun loadRecipeDetail(recipeId: Int) {
        viewModelScope.launch {
            when (val response = mushroomRepository.getRecipeDetail(recipeId)) {
                is Result.Error -> {}
                is Result.Success ->  _recipeDetail.value = response.response!!
            }
        }
    }
}

class CookingRecipesViewModelFactory(private val mushroomRepository: MushroomRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CookingRecipesViewModel::class.java)) {
            return CookingRecipesViewModel(mushroomRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

