package capstone.project.mushymatch.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import capstone.project.mushymatch.api.repository.AuthRepository
import capstone.project.mushymatch.api.repository.Result
import kotlinx.coroutines.launch


/*
   Created by Andre Eka Putra on 22/11/23
   andremoore431@gmail.com
*/

class LoginViewModel(
    private val repository: AuthRepository
): ViewModel() {
    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(username: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            when (val response = repository.login(username = username, password = password)) {
                is Result.Error -> {
                    _isError.value = true
                    _message.value = response.message
                    _isLoading.value = false
                }
                is Result.Success -> {
                    _isError.value = false
                    _message.value = response.response.message
                    _isLoading.value = false
                }
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory(private val authRepository: AuthRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

