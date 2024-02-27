package capstone.project.mushymatch.view.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import capstone.project.mushymatch.api.repository.MushroomRepository
import capstone.project.mushymatch.api.repository.Result
import capstone.project.mushymatch.api.response.mushroom.DetailMushroomResponse
import capstone.project.mushymatch.api.response.recipe.ListRecipesResponse
import kotlinx.coroutines.launch

class MushroomInformationViewModel(private val mushroomRepository: MushroomRepository) : ViewModel() {

    private val _mushroomDetail = MutableLiveData<DetailMushroomResponse>()
    val mushroomDetail: LiveData<DetailMushroomResponse> = _mushroomDetail

    private val _recipes = MutableLiveData<ListRecipesResponse>()
    val recipes: LiveData<ListRecipesResponse> = _recipes

    fun getMushroomDetail(mushroomId: Int) {
        viewModelScope.launch {
            when (val response = mushroomRepository.getMushroomDetail(mushroomId)) {
                is Result.Success -> _mushroomDetail.value = response.response!!
                is Result.Error -> {}
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class MushroomInformationViewModelFactory(private val mushroomRepository: MushroomRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MushroomInformationViewModel::class.java)) {
            return MushroomInformationViewModel(mushroomRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


