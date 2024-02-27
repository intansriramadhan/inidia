package capstone.project.mushymatch.helper

import android.app.Activity
import android.app.Instrumentation
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage

object GetActivityInstance {
    // Fungsi untuk mendapatkan instance aktivitas yang sedang berjalan
    fun getCurrentActivity(instrumentation: Instrumentation): Activity? {
        var currentActivity: Activity? = null

        instrumentation.runOnMainSync {
            val resumedActivities: Collection<Activity> =
                ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED)

            if (resumedActivities.isNotEmpty()) {
                currentActivity = resumedActivities.iterator().next()
            }
        }

        return currentActivity
    }
}