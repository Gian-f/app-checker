package com.br.appchecker.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.br.appchecker.R
import com.br.appchecker.databinding.ActivityLoginBinding
import com.br.appchecker.ui.login.viewmodels.LoginViewModel
import com.br.appchecker.ui.login.viewmodels.factory.LoginViewModelFactory
import com.br.appchecker.ui.questions.MainActivity
import com.br.appchecker.util.afterTextChanged

class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by
    lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        loginViewModel =
            ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]
        setupObservers()
        setupListeners()
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

        loginViewModel.loginResult.observe(this, Observer { loginResult ->
            loginResult ?: return@Observer
            with(binding) {
                loading?.visibility = View.GONE
                loginResult.error?.let { showLoginFailed(it) }
                loginResult.success?.let { updateUiWithUser(it) }
            }
            setResult(Activity.RESULT_OK)
            finish()
        })
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
                        loginViewModel.login(binding.username.text.toString(), text.toString()) {

                        }
                    }
                    false
                }
            }
        }

        with(binding) {
            login.setOnClickListener {
                loading?.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password.text.toString()) {
                    navigateToMain()
                }
            }

            guest?.setOnClickListener {
                navigateToMain()
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        Toast.makeText(applicationContext, "$welcome $displayName", Toast.LENGTH_LONG).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}