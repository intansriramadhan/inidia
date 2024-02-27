package capstone.project.mushymatch.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import capstone.project.mushymatch.api.repository.AuthRepository
import capstone.project.mushymatch.api.repository.Result
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: AuthRepository
): ViewModel() {
    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message
    fun register(email: String, password: String) {
        viewModelScope.launch {
            when (val response = repository.register(email = email, password = password)) {
                is Result.Error -> {
                    _isError.value = true
                    _message.value = response.message
                }
                is Result.Success -> {
                    _isError.value = false
                    _message.value = response.response.message
                }
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class RegisterViewModelFactory(private val authRepository: AuthRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

