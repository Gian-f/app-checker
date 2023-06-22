package com.br.appchecker.ui.login.viewmodels

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.appchecker.R
import com.br.appchecker.data.remote.response.LoginResponse
import com.br.appchecker.data.repository.login.LoginRepositoryImpl
import com.br.appchecker.data.state.StateLogin
import com.br.appchecker.ui.login.state.LoginFormState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val loginRepository: LoginRepositoryImpl,
) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<StateLogin<LoginResponse>>()
    val loginResult: LiveData<StateLogin<LoginResponse>> = _loginResult

    fun login(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            Log.e("ERRO login ", "$throwable") }) {
            deleteAllUsers()
            val state = loginRepository.login(username, password)
            withContext(Dispatchers.Main) {
                _loginResult.value = state
            }
        }
    }

    fun deleteAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            loginRepository.deleteAllUsers()
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

    private fun isUserNameValid(username: String): Boolean {
        val emailRegex = Patterns.EMAIL_ADDRESS.toRegex()
        return username.isNotBlank() && (emailRegex.matches(username))
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 5
    }
}