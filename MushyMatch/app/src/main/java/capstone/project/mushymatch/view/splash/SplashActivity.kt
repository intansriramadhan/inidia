package capstone.project.mushymatch.view.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import capstone.project.mushymatch.databinding.ActivitySplashBinding
import capstone.project.mushymatch.view.login.LoginActivity
import capstone.project.mushymatch.view.home.HomePageActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        // Initialize FirebaseApp
        FirebaseApp.initializeApp(this@SplashActivity)

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is already logged in, redirect to homepage
            redirectToHomePage()
        } else {
            // User is not logged in, redirect to login activity
            redirectToLoginActivity()
        }
    }

    private fun redirectToHomePage() {
        Handler().postDelayed({
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
            finish() // Close the splash activity to prevent going back to it using the back button
        }, 2000)
    }

    private fun redirectToLoginActivity() {
        Handler().postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Close the splash activity to prevent going back to it using the back button
        }, 2000)
    }
}