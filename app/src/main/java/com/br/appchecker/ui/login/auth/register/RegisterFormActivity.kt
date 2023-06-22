package com.br.appchecker.ui.login.auth.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.br.appchecker.R
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
                        true
                    } else {
                        false
                    }
                }
            }

            continueButton.setOnClickListener {
                val isPasswordValid = loginViewModel.isPasswordValid(password.text.toString())
                if (isPasswordValid) {
                    loading.visibility = View.VISIBLE
                    continueButton.isEnabled = false
                    startLoginActivityDelayed()
                } else {
                    loading.visibility = View.VISIBLE
                    continueButton.isEnabled = false
                    showPasswordLayoutDelayed()
                }
            }
        }
    }

    private fun setupObservers() {
        loginViewModel.loginFormState.observe(this, Observer { loginState ->
            loginState ?: return@Observer
            binding.apply {
                val isUsernameValid = loginViewModel.isUserNameValid(username.text.toString())
                val isPasswordValid = if (isUsernameValid) {
                    loginViewModel.isPasswordValid(password.text.toString())
                } else {
                    false
                }
                continueButton.isEnabled = isUsernameValid or isPasswordValid
                usernameLayout.error = loginState.usernameError?.let { getString(it) }
                passwordLayout.error = loginState.passwordError?.let { getString(it) }
            }
        })
    }

    private fun startLoginActivityDelayed() {
        binding.loading.visibility = View.VISIBLE
        binding.continueButton.postDelayed({
            binding.loading.visibility = View.GONE
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            showToast("Conta criada com sucesso!")
        }, 2000)
    }

    private fun showPasswordLayoutDelayed() {
        binding.apply {
            loading.visibility = View.VISIBLE
            passwordLayout.postDelayed({
                loading.visibility = View.GONE
                passwordLayout.visibility = View.VISIBLE
                usernameLayout.visibility = View.INVISIBLE
                letsGetStarted.text = getString(R.string.now)
                question.text = getString(R.string.create_password)
            }, 2000)
        }
    }
}