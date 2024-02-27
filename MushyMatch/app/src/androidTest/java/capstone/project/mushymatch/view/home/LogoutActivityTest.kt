package capstone.project.mushymatch.view.home

import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import capstone.project.mushymatch.databinding.ActivityLogoutBinding
import capstone.project.mushymatch.view.login.LoginActivity
import capstone.project.mushymatch.view.logout.LogoutActivity
import org.junit.After
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith



@RunWith(AndroidJUnit4::class)
class LogoutActivityTest {
    @get:Rule
    val rule = ActivityScenarioRule(LogoutActivity::class.java)
    private lateinit var binding: ActivityLogoutBinding

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
    fun logoutAndMoveToLoginActivity() {
        Espresso.onView(ViewMatchers.withId(binding.btnLogout.id)).perform(ViewActions.click())

        Intents.intended(IntentMatchers.hasComponent(LoginActivity::class.java.name))
    }
}