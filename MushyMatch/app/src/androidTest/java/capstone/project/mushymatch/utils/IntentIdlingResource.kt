package capstone.project.mushymatch.utils

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.rules.ActivityScenarioRule

//class IntentIdlingResource<T: Activity, V: View?>(
//    private val rule: ActivityScenarioRule<T>? = null,
//    private var component: V? = null,
//    private var intent: MutableList<Intent>,
//)


class IntentIdlingResource(
    private var intent: MutableList<Intent>,
): IdlingResource {

    private var resourceCallback: IdlingResource.ResourceCallback? = null
    private var isIdle = false

    override fun getName(): String? {
        return IntentIdlingResource::class.java.name
    }

    override fun isIdleNow(): Boolean {
        isIdle = intent.isNotEmpty()

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

inline fun <T> wrapIntentIdlingResource(
    intent: MutableList<Intent>,
    function: () -> T) : T {
    val intentIdling = IntentIdlingResource(intent)
    IdlingRegistry.getInstance().register(intentIdling)
    return try {
        function()
    } finally {
        IdlingRegistry.getInstance().unregister(intentIdling)
    }
}

//inline fun <T, reified V: View> wrapIntentIdlingResource(
//    rule: ActivityScenarioRule<*>? = null,
//    component: V? = null,
//    intent: MutableList<Intent>,
//    function: () -> T) : T {
//    val intentIdling = IntentIdlingResource(rule, component, intent)
//    IdlingRegistry.getInstance().register(intentIdling)
//    return try {
//        function()
//    } finally {
//        IdlingRegistry.getInstance().unregister(intentIdling)
//    }
//}
