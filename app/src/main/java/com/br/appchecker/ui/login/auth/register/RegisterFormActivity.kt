package com.br.appchecker.ui.login.auth.register

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.br.appchecker.data.local.AppDatabase
import com.br.appchecker.databinding.ActivityRegisterFormBinding
import com.br.appchecker.ui.login.auth.LoginActivity
import com.br.appchecker.ui.login.viewmodels.LoginViewModel
import com.br.appchecker.ui.login.viewmodels.factory.LoginViewModelFactory
import com.br.appchecker.util.afterTextChanged
import com.br.appchecker.util.showToast

class RegisterFormActivity : AppCompatActivity() {

    private val binding: ActivityRegisterFormBinding by lazy {
        ActivityRegisterFormBinding.inflate(layoutInflater)
    }

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupViewModel()
        setupListeners()
        setupObservers()
    }

    private fun setupViewModel() {
        val userDao = AppDatabase.getInstance(this).userDao()
        val viewModelFactory = LoginViewModelFactory(userDao)
        loginViewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
    }

    private fun setupListeners() {
        binding.apply {
            username.afterTextChanged { email ->
                loginViewModel.loginDataChanged(email, password.text.toString())
            }

            password.apply {
                afterTextChanged { password ->
                    loginViewModel.loginDataChanged(username.text.toString(), password)
                }
                setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        loginViewModel.login(username.text.toString(), text.toString())
                    }
                    false
                }
            }

            continueButton.setOnClickListener {
                val isPasswordVisible = (passwordLayout.visibility == View.VISIBLE)
                val isPasswordNotEmpty = password.text.isNotEmpty()

                if (isPasswordVisible && isPasswordNotEmpty) {
                    loading.visibility = View.VISIBLE
                    startLoginActivity()
                    showToast("Conta criada com sucesso!")
                } else {
                    loading.visibility = View.VISIBLE
                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed({
                        showPasswordLayout()
                    }, 2000)
                }
            }
        }
    }

    private fun setupObservers() {
        loginViewModel.loginFormState.observe(this, Observer { loginState ->
            loginState ?: return@Observer
            with(binding) {
                val isUsernameValid = (loginState.usernameError != 0)
                continueButton.isEnabled = isUsernameValid
                usernameLayout.error = loginState.usernameError?.let { getString(it) }
                passwordLayout.error = loginState.passwordError?.let { getString(it) }
            }
        })
    }

    private fun startLoginActivity() {
        binding.loading.visibility = View.GONE
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun showPasswordLayout() {
        binding.loading.visibility = View.GONE
        binding.apply {
            passwordLayout.visibility = View.VISIBLE
            usernameLayout.visibility = View.INVISIBLE
            letsGetStarted.text = "Agora..."
            question.text = "Crie uma senha"
        }
    }
}