package capstone.project.mushymatch.view.login

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import capstone.project.mushymatch.api.repository.AuthRepository
import capstone.project.mushymatch.databinding.ActivityHomePageBinding
import capstone.project.mushymatch.databinding.ActivityLoginBinding
import capstone.project.mushymatch.helper.GetActivityInstance.getCurrentActivity
import capstone.project.mushymatch.utils.wrapIntentIdlingResource
import capstone.project.mushymatch.utils.wrapShimmerIdlingResource
import capstone.project.mushymatch.utils.wrapViewModelIdlingResource
import capstone.project.mushymatch.view.home.HomePageActivity
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LoginActivityTest {
    @get:Rule
    val rule = ActivityScenarioRule(LoginActivity::class.java)
    private lateinit var loginBinding: ActivityLoginBinding
    private lateinit var homeBinding: ActivityHomePageBinding
    private lateinit var loginViewModel: LoginViewModel
    private var activity: Activity? = null

    @Before
    fun setUp() {
        Intents.init()
        rule.scenario.onActivity { activity ->
            loginBinding = activity.binding
            val repository = AuthRepository()
            val viewModelFactory = LoginViewModelFactory(repository)
            this.activity = activity

            // Use ViewModelProvider to create the ViewModel
            loginViewModel = ViewModelProvider(activity, viewModelFactory)[LoginViewModel::class.java]
        }
    }

    @After
    fun tearDown() {
        Intents.release()
        rule.scenario.close()
    }

    @Test
    fun loginSuccessMoveToHomeActivity() {
        // Type text into the name field
        Espresso.onView(ViewMatchers.withId(loginBinding.username.id))
            .perform(ViewActions.typeText("jkl@gmail.com"), ViewActions.closeSoftKeyboard())

        // Type text into the password field
        Espresso.onView(ViewMatchers.withId(loginBinding.password.id))
            .perform(ViewActions.typeText("jkl123"), ViewActions.closeSoftKeyboard())

        wrapViewModelIdlingResource(loginViewModel) {
            Espresso.onView(ViewMatchers.withId(loginBinding.btnLogin.id)).perform(ViewActions.click())
        }

        activity?.run { Log.d("hello", this::class.java.name) }
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        wrapIntentIdlingResource(Intents.getIntents()) {
            activity = getCurrentActivity(instrumentation)

            homeBinding = (activity as HomePageActivity).binding

        }

        wrapShimmerIdlingResource(homeBinding.shimmerViewContainer) {
            Intents.intended(IntentMatchers.hasComponent(HomePageActivity::class.java.name))
        }
    }


}