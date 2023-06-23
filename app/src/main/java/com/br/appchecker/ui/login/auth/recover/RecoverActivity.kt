package com.br.appchecker.ui.login.auth.recover

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.br.appchecker.data.local.AppDatabase
import com.br.appchecker.databinding.ActivityRecoverBinding
import com.br.appchecker.ui.login.viewmodels.LoginViewModel
import com.br.appchecker.ui.login.viewmodels.factory.LoginViewModelFactory
import com.br.appchecker.util.afterTextChanged

class RecoverActivity : AppCompatActivity() {

    private val binding: ActivityRecoverBinding by lazy {
        ActivityRecoverBinding.inflate(layoutInflater)
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
            email.afterTextChanged {
                loginViewModel.loginDataChanged(it, email.text.toString())
            }

            continueButton.setOnClickListener {
                val isemailValid = loginViewModel.isEmailValid(email.text.toString())
                if (isemailValid) {
                    loading.visibility = View.VISIBLE
                    continueButton.isEnabled = false
//                    startLoginActivityDelayed()
                    println(binding.email.text)
                } else {
                    loading.visibility = View.VISIBLE
                    continueButton.isEnabled = false
//                    showPasswordLayoutDelayed()
                }
            }
        }
    }

    private fun setupObservers() {
        loginViewModel.loginFormState.observe(this, Observer { loginState ->
            loginState ?: return@Observer
            binding.apply {
                val isEmailValid = loginViewModel.isEmailValid(email.text.toString())
                continueButton.isEnabled = isEmailValid
                emailLayout.error = loginState.usernameError?.let { getString(it) }
            }
        })
    }
}