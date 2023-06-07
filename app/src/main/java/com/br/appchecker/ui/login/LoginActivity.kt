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
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.br.appchecker.R
import com.br.appchecker.databinding.ActivityLoginBinding
import com.br.appchecker.ui.questions.MainActivity
import com.br.appchecker.util.afterTextChanged


class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        loginViewModel =
            ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]
        setupLoginFormStateObserver()
        setupLoginResultObserver()
        setupUsernameTextChangedListener()
        setupPasswordTextChangedListener()
        setupLoginButtonClickListener()
        setupGuestClickListener()
    }

    private fun setupLoginFormStateObserver() {
        loginViewModel.loginFormState.observe(this@LoginActivity, Observer { loginState ->
            loginState ?: return@Observer
            binding.login.isEnabled = loginState.isDataValid
            binding.usernameLayout?.error = loginState.usernameError?.let { getString(it) }
            binding.usernameLayout?.error = if (binding.username.text.isNotEmpty()) null else binding.usernameLayout?.error
            binding.passwordLayout?.error = if (binding.password.text.isNotEmpty()) null else binding.passwordLayout?.error
            binding.passwordLayout?.error = loginState.passwordError?.let { getString(it) }
        })
    }

    private fun setupLoginResultObserver() {
        loginViewModel.loginResult.observe(this@LoginActivity, Observer { loginResult ->
            loginResult ?: return@Observer

            binding.loading?.visibility = View.GONE

            loginResult.error?.let { showLoginFailed(it) }
            loginResult.success?.let { updateUiWithUser(it) }

            setResult(Activity.RESULT_OK)

            finish()
        })
    }

    private fun setupUsernameTextChangedListener() {
        binding.username.afterTextChanged {
            loginViewModel.loginDataChanged(binding.username.text.toString(), binding.password.text.toString())
        }
    }

    private fun setupPasswordTextChangedListener() {
        binding.password.afterTextChanged {
            loginViewModel.loginDataChanged(binding.username.text.toString(), binding.password.text.toString())
        }

        binding.password.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(binding.username.text.toString(), binding.password.text.toString())
            }
            false
        }
    }

    private fun setupLoginButtonClickListener() {
        binding.login.setOnClickListener {
            binding.loading?.visibility = View.VISIBLE
            loginViewModel.login(binding.username.text.toString(), binding.password.text.toString())
        }
    }

    private fun setupGuestClickListener() {
        binding.guest?.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
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