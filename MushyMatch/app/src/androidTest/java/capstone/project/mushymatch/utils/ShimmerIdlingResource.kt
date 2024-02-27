package capstone.project.mushymatch.utils

import android.util.Log
import androidx.core.view.isGone
import androidx.databinding.ViewDataBinding
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import com.facebook.shimmer.ShimmerFrameLayout

class ShimmerIdlingResource(private var shimmerFrameLayout: ShimmerFrameLayout) : IdlingResource {

    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName(): String? {
        return ShimmerIdlingResource::class.java.name
    }

    override fun isIdleNow(): Boolean {
        var isIdle = shimmerFrameLayout.isGone

        Log.d("hello", "isIdle = $isIdle")
        if (!isIdle && resourceCallback != null) {
            resourceCallback?.onTransitionToIdle()
        }
        return isIdle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        resourceCallback = callback
    }

}

inline fun <T> wrapShimmerIdlingResource(shimmerFrameLayout: ShimmerFrameLayout,function: () -> T) : T {
    val shimmerIdling = ShimmerIdlingResource(shimmerFrameLayout)
    IdlingRegistry.getInstance().register(shimmerIdling)
    return try {
        function()
    } finally {
        IdlingRegistry.getInstance().unregister(shimmerIdling)
    }
}