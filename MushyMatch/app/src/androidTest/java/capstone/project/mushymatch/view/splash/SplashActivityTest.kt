package capstone.project.mushymatch.view.splash

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import capstone.project.mushymatch.databinding.ActivityRegisterBinding
import capstone.project.mushymatch.databinding.ActivitySplashBinding
import capstone.project.mushymatch.view.register.RegisterActivity
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SplashActivityTest {

    @get:Rule
    val rule = ActivityScenarioRule(SplashActivity::class.java)
    private lateinit var binding: ActivitySplashBinding

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
    fun testSplashScreenDisplayed() {
        onView(withId(binding.imageView5.id)).check(matches(isDisplayed()))
    }
}