package com.br.appchecker.presentation.login.auth.register

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.br.appchecker.R
import com.br.appchecker.data.local.AppDatabase
import com.br.appchecker.data.state.StateInfo
import com.br.appchecker.databinding.ActivityRegisterFormBinding
import com.br.appchecker.presentation.login.auth.LoginActivity
import com.br.appchecker.presentation.login.viewmodels.LoginViewModel
import com.br.appchecker.presentation.login.viewmodels.factory.LoginViewModelFactory
import com.br.appchecker.util.FirebaseUtils
import com.br.appchecker.util.LoadingUtils
import com.br.appchecker.util.LoadingUtils.showErrorSheet
import com.br.appchecker.util.ValidationUtils.isEmailValid
import com.br.appchecker.util.ValidationUtils.isNameValid
import com.br.appchecker.util.ValidationUtils.isPasswordValid
import com.br.appchecker.util.afterTextChanged
import com.br.appchecker.util.showNotification
import com.br.appchecker.util.showToast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuthException

class RegisterFormActivity : AppCompatActivity() {

    private val binding: ActivityRegisterFormBinding by lazy {
        ActivityRegisterFormBinding.inflate(
            layoutInflater
        )
    }

    private lateinit var loginViewModel: LoginViewModel

    private var bottomSheetDialog: BottomSheetDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupViewModel()
        setupListeners()
        setupObservers()
        setupEmailFocus()
    }

    private fun setupViewModel() {
        val userDao = AppDatabase.getInstance(this).userDao()
        val viewModelFactory = LoginViewModelFactory(userDao)
        loginViewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
    }

    private fun setupListeners() {
        with(binding) {
            email.afterTextChanged { email ->
                updateLoginForm(email, password.text.toString(), name.text.toString())
            }

            password.afterTextChanged { password ->
                updateLoginForm(email.text.toString(), password, name.text.toString())
            }

            name.afterTextChanged { name ->
                updateLoginForm(email.text.toString(), password.text.toString(), name)
            }

            continueButton.setOnClickListener {
                val isEmailValid = isEmailValid(email.text.toString())
                val isPasswordValid = isPasswordValid(password.text.toString())
                val isNameValid = isNameValid(name.text.toString())
                continueButton.isEnabled = false

                if (isEmailValid) {
                    showPasswordLayoutDelayed()
                }
                if (isPasswordValid) {
                    showNameLayoutDelayed()
                }
                if (isNameValid) {
                    startLoginActivityDelayed()
                }
            }
        }
    }

    private fun setupEmailFocus() {
        binding.email.requestFocus()
    }

    private fun updateLoginForm(email: String, password: String, name: String) {
        loginViewModel.createUserDataChanged(email, password, name)
    }

    private fun setupObservers() {
        loginViewModel.loginFormState.observe(this, Observer { loginState ->
            loginState ?: return@Observer
            binding.apply {
                val isUsernameValid = isEmailValid(email.text.toString())
                val isPasswordValid = if (isUsernameValid) {
                    isPasswordValid(password.text.toString())
                } else false
                val isNameValid = isNameValid(name.text.toString())
                continueButton.isEnabled = isUsernameValid or isPasswordValid or isNameValid
                emailLayout.error = loginState.usernameError?.let { getString(it) }
                passwordLayout.error = loginState.passwordError?.let { getString(it) }
                nameLayout.error = loginState.nameError?.let { getString(it) }
            }
        })

        loginViewModel.userResult.observe(this) { state ->
            when (state) {
                is StateInfo.Success -> {
                    hideLoading()
                    showNotification(
                        "Obrigado por fazer parte, ${binding.name.text}!",
                        "Sua conta foi cadastrada com sucesso!"
                    )
                    showToast("Seja bem-vindo, ${binding.name.text}!")
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    setResult(Activity.RESULT_OK)
                }

                is StateInfo.Error -> {
                    hideLoading()

                    val errorMessage = when (state.exception) {
                        is FirebaseAuthException -> {
                            FirebaseUtils.getErrorMessage(state.exception)
                        }
                        else -> state.message
                    }

                    showErrorSheet(this, message = errorMessage)
                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed({
                        val intent = Intent(this, RegisterFormActivity::class.java)
                        startActivity(intent)
                        finish()
                    }, 3000)
                }
            }
        }

    }

    private fun startLoginActivityDelayed() {
        closeKeyboard(this@RegisterFormActivity)
        binding.apply {
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                showLoading()
            }, 500)
            continueButton.postDelayed({
                val email = binding.email.text.toString().trim()
                val password = binding.password.text.toString().trim()
                val name = binding.name.text.toString().trim()

                loginViewModel.insertUserFirebase(email, password, name)
            }, 2000)
        }
    }

    private fun showPasswordLayoutDelayed() {
        binding.apply {
            loading.visibility = View.VISIBLE
            passwordLayout.postDelayed({
                loading.visibility = View.GONE
                passwordLayout.visibility = View.VISIBLE
                emailLayout.visibility = View.INVISIBLE
                password.requestFocus()
                letsGetStarted.text = getString(R.string.now)
                question.text = getString(R.string.create_password)
            }, 800)
        }
    }

    private fun showNameLayoutDelayed() {
        binding.apply {
            loading.visibility = View.VISIBLE
            nameLayout.postDelayed({
                loading.visibility = View.GONE
                passwordLayout.visibility = View.INVISIBLE
                nameLayout.visibility = View.VISIBLE
                name.requestFocus()
                letsGetStarted.text = getString(R.string.finalize)
                question.text = getString(R.string.create_name)
            }, 800)
        }
    }

    private fun closeKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusView = activity.currentFocus
        if (currentFocusView != null) {
            imm.hideSoftInputFromWindow(currentFocusView.windowToken, 0)
        }
    }

    private fun showLoading() {
        bottomSheetDialog = LoadingUtils.showLoadingSheet(this)
    }

    private fun hideLoading() {
        bottomSheetDialog?.let { LoadingUtils.dismissLoadingSheet(it) }
        bottomSheetDialog = null
    }

    override fun onDestroy() {
        super.onDestroy()
        hideLoading()
        bottomSheetDialog?.dismiss()
    }
}