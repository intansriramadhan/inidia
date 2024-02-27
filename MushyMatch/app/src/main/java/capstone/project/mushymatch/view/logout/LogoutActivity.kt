package capstone.project.mushymatch.view.logout

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import capstone.project.mushymatch.api.repository.AuthRepository
import capstone.project.mushymatch.databinding.ActivityLogoutBinding
import capstone.project.mushymatch.view.login.LoginActivity

class LogoutActivity : AppCompatActivity() {
    lateinit var binding: ActivityLogoutBinding
    private lateinit var viewModel: LogoutViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogoutBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val repository = AuthRepository()
        val viewModelFactory = LogoutViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[LogoutViewModel::class.java]

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
        }

        viewModel.isError.observe(this) {
            if (!it) {
                moveToLoginActivity()
            }
        }

        viewModel.email.observe(this) {
            binding.emailText.text = it
        }
    }

    private fun moveToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Close the profile activity to prevent going back to it using the back button
    }
}
