package capstone.project.mushymatch.view.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import capstone.project.mushymatch.api.repository.AuthRepository
import capstone.project.mushymatch.databinding.ActivityRegisterBinding
import capstone.project.mushymatch.view.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = AuthRepository()
        val viewModelFactory = RegisterViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[RegisterViewModel::class.java]



        binding.btnregister.setOnClickListener{
            val nama = binding.username.text.toString()
            val emailText = binding.email.text.toString()
            val passwordText = binding.password.text.toString()

            if (nama.isEmpty()) {
                binding.username.error = "Silahkan Isi Username Anda"
                binding.username.requestFocus()
                return@setOnClickListener
            }

            if (emailText.isEmpty()) {
                binding.email.error = "Silahkan Isi Email Anda"
                binding.email.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                binding.email.error = "Email Tidak Valid"
                binding.email.requestFocus()
                return@setOnClickListener
            }

            if (passwordText.isEmpty()) {
                binding.password.error = "Silahkan Isi Password Anda"
                binding.password.requestFocus()
                return@setOnClickListener
            }

            if (passwordText.length < 6) {
                binding.password.error = "Password Minimal 6 Karakter"
                binding.password.requestFocus()
                return@setOnClickListener
            }

            viewModel.register(emailText, passwordText)
        }

        binding.btnMasuk.setOnClickListener {
            val login = Intent(this, LoginActivity::class.java)
            startActivity(login)
        }

        viewModel.message.observe(this) {
            showToastMessage(it)
        }

        viewModel.isError.observe(this) {
            if (!it) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }
    private fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}