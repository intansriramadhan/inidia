package capstone.project.mushymatch.utils

import android.app.Activity
import android.util.Log
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import capstone.project.mushymatch.view.login.LoginViewModel

class ViewModelIdlingResource(private val viewModel: LoginViewModel) : IdlingResource {

    private var resourceCallback: IdlingResource.ResourceCallback? = null
    private var isIdle = true

    override fun getName(): String? {
        return ViewModelIdlingResource::class.java.name
    }

    override fun isIdleNow(): Boolean {
        isIdle = !(viewModel.isLoading.value ?: false)// Check if the ViewModel task is complete

        if (!isIdle && resourceCallback != null) {
            resourceCallback?.onTransitionToIdle()
        }
        return isIdle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        resourceCallback = callback
    }
}

inline fun <T> wrapViewModelIdlingResource(viewModel: LoginViewModel, function: () -> T): T {
    val viewModelIdlingResource = ViewModelIdlingResource(viewModel)
    IdlingRegistry.getInstance().register(viewModelIdlingResource)
    return try {
        function()
    } finally {
        IdlingRegistry.getInstance().unregister(viewModelIdlingResource)
    }
}