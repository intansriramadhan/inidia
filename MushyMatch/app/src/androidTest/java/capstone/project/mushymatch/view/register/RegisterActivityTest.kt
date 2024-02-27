package capstone.project.mushymatch.view.register

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import capstone.project.mushymatch.databinding.ActivityRegisterBinding
import capstone.project.mushymatch.view.login.LoginActivity
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith



@RunWith(AndroidJUnit4::class)
class RegisterActivityTest {
    @get:Rule
    val rule = ActivityScenarioRule(RegisterActivity::class.java)
    private lateinit var binding: ActivityRegisterBinding

    @Before
    fun setUp() {
        Intents.init()
        rule.scenario.onActivity { activity ->
            binding = activity.binding
        }
    }

    @After
    fun tearDown() {
        Intents.release()
        rule.scenario.close()
    }

    @Test
    fun registerSuccessMoveToLoginActivity() {
        // Type text into the name field
        onView(withId(binding.username.id)).perform(typeText("John Doe"), closeSoftKeyboard())

        // Type text into the email field
        onView(withId(binding.email.id)).perform(typeText("john.doe@example.com"), closeSoftKeyboard())

        // Type text into the password field
        onView(withId(binding.password.id)).perform(typeText("password123"), closeSoftKeyboard())

        // Click the register button
        onView(withId(binding.btnMasuk.id)).perform(click())

        // Check if the activity has switched to the LoginActivity
        intended(hasComponent(LoginActivity::class.java.name))
    }
}