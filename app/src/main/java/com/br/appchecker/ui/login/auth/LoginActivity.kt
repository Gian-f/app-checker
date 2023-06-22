package com.br.appchecker.ui.login.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.br.appchecker.R
import com.br.appchecker.data.local.AppDatabase
import com.br.appchecker.data.state.StateLogin
import com.br.appchecker.databinding.ActivityLoginBinding
import com.br.appchecker.ui.login.auth.recover.RecoverActivity
import com.br.appchecker.ui.login.auth.register.RegisterActivity
import com.br.appchecker.ui.login.viewmodels.LoginViewModel
import com.br.appchecker.ui.login.viewmodels.factory.LoginViewModelFactory
import com.br.appchecker.ui.questions.MainActivity
import com.br.appchecker.util.afterTextChanged
import com.br.appchecker.util.showBottomSheet

class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by
    lazy { ActivityLoginBinding.inflate(layoutInflater) }

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupFactory()
        setupObservers()
        setupListeners()
    }

    private fun setupFactory() {
        val userDao = AppDatabase.getInstance(this).userDao()
        val viewModelFactory = LoginViewModelFactory(userDao)
        loginViewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
    }

    private fun setupObservers() {
        loginViewModel.loginFormState.observe(this, Observer { loginState ->
            loginState ?: return@Observer
            with(binding) {
                login.isEnabled = loginState.isDataValid
                usernameLayout?.error = loginState.usernameError?.let { getString(it) }
                passwordLayout?.error = loginState.passwordError?.let { getString(it) }
            }
        })

        loginViewModel.loginResult.observe(this) { state ->
            when (state) {
                is StateLogin.Success -> {
                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed({
                        navigateToMain()
                    }, 3000)
                }

                is StateLogin.Error -> {
                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed({
                        showLoginFailed()
                    }, 3000)
                }
            }
            setResult(Activity.RESULT_OK)
        }
    }

    private fun setupListeners() {
        binding.apply {

            username.afterTextChanged { text ->
                loginViewModel.loginDataChanged(text, binding.password.text.toString())
            }

            password.apply {
                afterTextChanged { text ->
                    loginViewModel.loginDataChanged(binding.username.text.toString(), text)
                }

                setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        loginViewModel.login(binding.username.text.toString(), text.toString())
                    }
                    false
                }
            }

            register?.setOnClickListener {
                val intent = Intent(
                    applicationContext,
                    RegisterActivity::class.java
                )
                startActivity(intent)
            }

            recover?.setOnClickListener {
                val intent = Intent(
                    this@LoginActivity,
                    RecoverActivity::class.java
                )
                startActivity(intent)
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password.text.toString())
            }

            guest?.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.loginAsGuest()
            }
        }
    }

    private fun navigateToMain() {
        binding.loading.visibility = View.GONE
        Toast.makeText(
            this,
            "Login Efetuado com sucesso!",
            Toast.LENGTH_LONG
        ).show()
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }

    private fun showLoginFailed() {
        binding.loading.visibility = View.GONE
        showBottomSheet(message = R.string.error_login)
    }

}