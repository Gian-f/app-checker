package com.br.appchecker.presentation.login.auth.recover

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.br.appchecker.R
import com.br.appchecker.data.local.AppDatabase
import com.br.appchecker.databinding.ActivityRecoverBinding
import com.br.appchecker.presentation.login.auth.LoginActivity
import com.br.appchecker.presentation.login.viewmodels.LoginViewModel
import com.br.appchecker.presentation.login.viewmodels.factory.LoginViewModelFactory
import com.br.appchecker.util.LoadingUtils.showBottomSheet
import com.br.appchecker.util.ValidationUtils.isCodeValid
import com.br.appchecker.util.ValidationUtils.isEmailValid
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
        val viewModelFactory = LoginViewModelFactory(userDao,this)
        loginViewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
    }

    private fun setupListeners() {
        with(binding) {
            email.afterTextChanged { text ->
                loginViewModel.loginDataChanged(text, email.text.toString())
            }

            code.number1.afterTextChanged { text ->
                loginViewModel.loginDataChanged(text, code.number1.text.toString())
            }

            code.number2.afterTextChanged { text ->
                loginViewModel.loginDataChanged(text, code.number2.text.toString())
            }

            code.number3.afterTextChanged { text ->
                loginViewModel.loginDataChanged(text, code.number3.text.toString())
            }

            code.number4.afterTextChanged { text ->
                loginViewModel.loginDataChanged(text, code.number4.text.toString())
            }

            tvResend.setOnClickListener {
                showBottomSheet(this@RecoverActivity, message = R.string.error_not_implemented_yet)
            }

            continueButton.setOnClickListener {
                val isEmailValid = isEmailValid(email.text.toString())

                loading.visibility = View.VISIBLE
                continueButton.isEnabled = false

                val code = listOf(
                    code.number1,
                    code.number2,
                    code.number3,
                    code.number4
                )

                code.forEachIndexed { index, editText ->
                    if (index < code.size - 1) {
                        val nextEditText = code[index + 1]
                        editText.addTextChangedListener(createTextWatcher(nextEditText))
                    }
                }

                if (isEmailValid) {
                    showResetCodeLayoutDelayed()
                } else if (isCodeValid(code)) {
                    startActivityDelayed()
                }
            }
        }
    }

    private fun createTextWatcher(nextEditText: EditText?): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1 && nextEditText != null) {
                    nextEditText.requestFocus()
                }
            }
        }
    }

    private fun setupObservers() {
        loginViewModel.loginFormState.observe(this) { loginState ->
            loginState ?: return@observe
            with(binding) {
                val isEmailValid = isEmailValid(email.text.toString())
                val code = listOf(code.number1, code.number2, code.number3, code.number4)
                val isCodeValid = isCodeValid(code)
                continueButton.isEnabled = isEmailValid || isCodeValid
                emailLayout.error = loginState.usernameError?.let { getString(it) }
            }
        }
    }

    private fun startActivityDelayed() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun showResetCodeLayoutDelayed() {
        with(binding) {
            loading.visibility = View.VISIBLE
            emailLayout.postDelayed({
                loading.visibility = View.GONE
                emailLayout.visibility = View.GONE
                tvTitle.text = getString(R.string.enter_the_code)
                tvDesc.textSize = 18F
                val emailText = email.text.toString()
                val codeDescription =
                    "Digite o código de 4 dígitos que enviamos para o e-mail: $emailText"
                tvDesc.text = codeDescription
                tvText.text = getString(R.string.dont_receive_code)
                tvText.gravity = Gravity.CENTER
                tvResend.visibility = View.VISIBLE
                code.root.visibility = View.VISIBLE
            }, 1000)
        }
    }
}