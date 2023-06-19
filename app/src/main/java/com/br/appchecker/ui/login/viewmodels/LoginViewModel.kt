package com.br.appchecker.ui.login.viewmodels

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.appchecker.R
import com.br.appchecker.data.model.login.StateLogin
import com.br.appchecker.data.repository.login.LoginRepositoryImpl
import com.br.appchecker.ui.login.LoggedInUserView
import com.br.appchecker.ui.login.LoginFormState
import com.br.appchecker.ui.login.LoginResult
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val loginRepository: LoginRepositoryImpl
) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String, isLogin: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable -> Log.e("ERRO login ", "$throwable") }) {
            val state = loginRepository.login(username, password)
            withContext(Dispatchers.Main) {
                when (state) {
                    is StateLogin.Success -> {
                        val loggedInUser = state.data?.user
                        if (loggedInUser != null) {
                            _loginResult.value = LoginResult(success = LoggedInUserView(displayName = loggedInUser.nome))
                            isLogin.invoke()
                        }
                    }
                    is StateLogin.Error -> {
                        _loginResult.value = LoginResult(error = R.string.login_failed)
                    }
                }
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 5
    }
}