package capstone.project.mushymatch.view.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import capstone.project.mushymatch.api.repository.MushroomRepository
import capstone.project.mushymatch.api.repository.Result
import capstone.project.mushymatch.api.response.mushroom.GetMushroomResponseItem
import kotlinx.coroutines.launch

class MushroomViewModel(private val repository: MushroomRepository) : ViewModel() {

    private val _mushrooms = MutableLiveData<List<GetMushroomResponseItem>>()
    val mushrooms: LiveData<List<GetMushroomResponseItem>> = _mushrooms

    fun loadMushrooms() {
        viewModelScope.launch {
                when (val response = repository.getMushrooms()) {
                    is Result.Error ->  Log.e("MushroomViewModel", response.message)
                    is Result.Success -> _mushrooms.value = response.response
                }
            }
        }
    }


class MushroomViewModelFactory(private val repository: MushroomRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MushroomViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MushroomViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

