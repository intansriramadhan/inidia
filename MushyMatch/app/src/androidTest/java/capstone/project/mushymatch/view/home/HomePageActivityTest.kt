package capstone.project.mushymatch.view.home

import android.app.Activity
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import capstone.project.mushymatch.view.about.MushroomInformationActivity
import capstone.project.mushymatch.databinding.ActivityHomePageBinding
import capstone.project.mushymatch.databinding.ActivityMushroomInformationBinding
import capstone.project.mushymatch.helper.GetActivityInstance.getCurrentActivity
import capstone.project.mushymatch.utils.wrapIntentIdlingResource

import capstone.project.mushymatch.utils.wrapShimmerIdlingResource
import org.hamcrest.CoreMatchers.containsString
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class HomePageActivityTest {
    @get:Rule
    val rule = ActivityScenarioRule(HomePageActivity::class.java)
    private lateinit var homeBinding: ActivityHomePageBinding
    private lateinit var detailBinding: ActivityMushroomInformationBinding
    private var keywordExpectation = "Enoki"
    private var activity: Activity? = null

    @Before
    fun setUp() {
        Intents.init()
        rule.scenario.onActivity { activity ->
            homeBinding = activity.binding
            this.activity = activity
        }
    }

    @After
    fun tearDown() {
        Intents.release()
        rule.scenario.close()
    }

    @Test
    fun searchAndTheResultIsProvided() {
        wrapShimmerIdlingResource(homeBinding.shimmerViewContainer) {
            Log.d("hello", "Before typeText1")
            onView(withId(homeBinding.searchView.id))
                .perform(ViewActions.typeText(keywordExpectation), ViewActions.closeSoftKeyboard())
            Log.d("hello", "Before typeText2")
        }

        onView(withId(homeBinding.rvMushrooms.id)).check(matches(isDisplayed()))
    }

    @Test
    fun clickOnFirstItemAndMoveToDetailActivity() {
        wrapShimmerIdlingResource(homeBinding.shimmerViewContainer) {
            onView(withId(homeBinding.searchView.id))
                .perform(ViewActions.typeText(keywordExpectation), ViewActions.closeSoftKeyboard())
        }

        onView(withId(homeBinding.rvMushrooms.id)).check(matches(isDisplayed()))

        onView(withId(homeBinding.rvMushrooms.id))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        activity?.run { Log.d("hello", this::class.java.name) }
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        wrapIntentIdlingResource(Intents.getIntents()) {
            activity = getCurrentActivity(instrumentation)

            // Dapatkan binding dari aktivitas yang sedang berjalan
            detailBinding = (activity as MushroomInformationActivity).binding
        }

        wrapShimmerIdlingResource(detailBinding.shimmerViewContainer) {
            Intents.intended(IntentMatchers.hasComponent(MushroomInformationActivity::class.java.name))
        }

        // Verifying whether tvMushroomName contains a String (keywordExpectation)
        onView(withId(detailBinding.tvMushroomName.id))
            .check(matches(withText(containsString(keywordExpectation))))


    }


}