package com.br.appchecker.presentation.login.auth

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.br.appchecker.R
import com.br.appchecker.data.local.AppDatabase
import com.br.appchecker.data.state.StateLogin
import com.br.appchecker.databinding.ActivityLoginBinding
import com.br.appchecker.presentation.login.auth.recover.RecoverActivity
import com.br.appchecker.presentation.login.auth.register.RegisterActivity
import com.br.appchecker.presentation.login.viewmodels.LoginViewModel
import com.br.appchecker.presentation.login.viewmodels.factory.LoginViewModelFactory
import com.br.appchecker.presentation.questions.MainActivity
import com.br.appchecker.util.FirebaseUtils
import com.br.appchecker.util.LoadingUtils
import com.br.appchecker.util.LoadingUtils.showBottomSheet
import com.br.appchecker.util.LoadingUtils.showErrorSheet
import com.br.appchecker.util.afterTextChanged
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuthException


class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by
    lazy { ActivityLoginBinding.inflate(layoutInflater) }

    private lateinit var loginViewModel: LoginViewModel

    private val REQUEST_CODE_PERMISSIONS = 0

    private var bottomSheetDialog: BottomSheetDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupPermissions()
        setupFactory()
        setupObservers()
        setupListeners()
    }

    private fun setupPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECEIVE_SMS
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WAKE_LOCK
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.VIBRATE
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // Se as permissões ainda não foram concedidas, solicita ao usuário
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.POST_NOTIFICATIONS,
                        Manifest.permission.VIBRATE
                    ),
                    REQUEST_CODE_PERMISSIONS
                )
            }
        } else {
            // As permissões já foram concedidas
            // Continue com a lógica de login ou exiba a tela principal
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissões concedidas, continue com a lógica de login ou exiba a tela principal
            } else {
                showBottomSheet(this, message = R.string.deny_permissions)
            }
        }
    }

    private fun setupFactory() {
        val userDao = AppDatabase.getInstance(this).userDao()
        val viewModelFactory = LoginViewModelFactory(userDao, this)
        loginViewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
    }

    private fun setupObservers() {
        loginViewModel.loginFormState.observe(this, Observer { loginState ->
            loginState ?: return@Observer
            with(binding) {
                login.isEnabled = loginState.isDataValid
                emailLayout?.error = loginState.usernameError?.let { getString(it) }
                passwordLayout?.error = loginState.passwordError?.let { getString(it) }
            }
        })

        loginViewModel.loginResult.observe(this) { state ->
            when (state) {
                is StateLogin.Success -> {
                    hideLoading()
                    navigateToMain()
                }

                is StateLogin.Error -> {
                    hideLoading()

                    val errorMessage = when (state.exception) {
                        is FirebaseAuthException -> {
                            FirebaseUtils.getErrorMessage(state.exception)
                        }
                        else -> state.message
                    }

                    binding.loading.visibility = View.GONE
                    showErrorSheet(this, message = errorMessage)
                }
            }
            setResult(Activity.RESULT_OK)
        }
    }


    private fun setupListeners() {

        binding.apply {
            email.afterTextChanged { text ->
                loginViewModel.loginDataChanged(text, binding.password.text.toString())
            }

            password.apply {
                afterTextChanged { text ->
                    loginViewModel.loginDataChanged(binding.email.text.toString(), text)
                }

                setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        loginViewModel.loginFirebase(binding.email.text.toString(), text.toString())
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
                showLoading()
                loginViewModel.deleteAllUsers()
                loginViewModel.loginFirebase(email.text.toString(), password.text.toString())
            }

            guest?.setOnClickListener {
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
        finish()
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }

    private fun showLoading() {
        bottomSheetDialog = LoadingUtils.showLoadingSheet(this)
    }

    private fun hideLoading() {
        bottomSheetDialog?.let { LoadingUtils.dismissLoadingSheet(it) }
        bottomSheetDialog = null
    }
}