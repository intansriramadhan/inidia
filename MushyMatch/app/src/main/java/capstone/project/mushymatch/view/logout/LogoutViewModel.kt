package capstone.project.mushymatch.view.logout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import capstone.project.mushymatch.api.repository.AuthRepository
import capstone.project.mushymatch.api.repository.Result
import kotlinx.coroutines.launch


class LogoutViewModel(
    private val repository: AuthRepository
): ViewModel() {
    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    init {
        viewModelScope.launch {
            when (val response = repository.getCurrentUserEmail()) {
                is Result.Error -> {}
                is Result.Success -> _email.value = response.response!!
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            when (val response = repository.logout()) {
                is Result.Error -> {
                    _isError.value = true
                    _message.value = response.message
                }
                is Result.Success -> _isError.value = false
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class LogoutViewModelFactory(private val authRepository: AuthRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LogoutViewModel::class.java)) {
            return LogoutViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}