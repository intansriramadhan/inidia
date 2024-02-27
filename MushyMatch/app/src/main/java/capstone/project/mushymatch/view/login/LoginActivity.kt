package capstone.project.mushymatch.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import capstone.project.mushymatch.api.repository.AuthRepository
import capstone.project.mushymatch.databinding.ActivityLoginBinding
import capstone.project.mushymatch.view.register.RegisterActivity
import capstone.project.mushymatch.view.home.HomePageActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = AuthRepository()
        val viewModelFactory = LoginViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]


        binding.btnLogin.setOnClickListener {
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()

            if (username.isEmpty()) {
                binding.username.error = "Silahkan Isi Username Anda"
                binding.username.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.password.error = "Silahkan Isi Password Anda"
                binding.password.requestFocus()
                return@setOnClickListener
            }

            viewModel.login(username, password)
        }

        binding.textRegister.setOnClickListener {
            val register = Intent(this, RegisterActivity::class.java)
            startActivity(register)
        }

        viewModel.message.observe(this) {
            showToastMessage(it)
        }

        viewModel.isError.observe(this) {
            if (!it) {
                val intent = Intent(this, HomePageActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}
