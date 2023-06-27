package com.br.appchecker.presentation.login.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.appchecker.R
import com.br.appchecker.data.remote.response.LoginResponse
import com.br.appchecker.data.remote.response.UserResponse
import com.br.appchecker.data.repository.login.LoginRepositoryImpl
import com.br.appchecker.data.state.StateInfo
import com.br.appchecker.data.state.StateLogin
import com.br.appchecker.presentation.login.state.LoginFormState
import com.br.appchecker.util.ValidationUtils.isEmailValid
import com.br.appchecker.util.ValidationUtils.isNameValid
import com.br.appchecker.util.ValidationUtils.isPasswordValid
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class LoginViewModel(
    private val loginRepository: LoginRepositoryImpl,
) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<StateLogin<LoginResponse>>()
    val loginResult: LiveData<StateLogin<LoginResponse>> = _loginResult

    private val _userResult = MutableLiveData<StateInfo<UserResponse>>()
    val userResult: LiveData<StateInfo<UserResponse>> = _userResult

    fun login(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            Log.e("ERRO login ", "$throwable")
        }) {
            val state = loginRepository.login(username, password)
            withContext(Dispatchers.Main) {
                _loginResult.value = state
            }
        }
    }

    fun loginAsGuest() {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            Log.e("ERRO login as guest ", "$throwable")
        }) {
            val state = loginRepository.loginAsGuest()
            withContext(Dispatchers.Main) {
                _loginResult.value = state
            }
        }
    }

    fun insertUser(email: String, password: String, name: String) {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            Log.e("ERRO ao criar usuário", "$throwable")
            val errorState = handleError(throwable)
            _userResult.value = errorState
        }) {
            val state = loginRepository.createUser(email, password, name)
            withContext(Dispatchers.Main) {
                _userResult.value = state
            }
        }
    }

    private fun handleError(throwable: Throwable): StateInfo<UserResponse> {
        return when (throwable) {
            is HttpException -> {
                val code = throwable.code()
                val message = throwable.message()
                when (code) {
                    404 -> {
                        if (message == "O usuário já existe") {
                            StateInfo.Error(
                                message = "O usuário já existe",
                                txt = "Por favor, tente novamente",
                                title = "Erro ao criar usuário"
                            )
                        } else {
                            StateInfo.Error(
                                message = "Erro ao criar usuário. Código de resposta: $code",
                                code = code,
                                txt = "Por favor, tente novamente",
                                title = "Erro ao criar usuário"
                            )
                        }
                    }

                    else -> {
                        StateInfo.Error(
                            message = "Erro ao criar usuário. Código de resposta: $code",
                            code = code,
                            txt = "Por favor, tente novamente",
                            title = "Erro ao criar usuário"
                        )
                    }
                }
            }

            else -> {
                StateInfo.Error(
                    message = "Erro ao criar usuário",
                    exception = throwable
                )
            }
        }
    }

    fun deleteAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            loginRepository.deleteAllUsers()
        }
    }

    fun loginDataChanged(username: String, password: String) {
        _loginForm.value = if (!isEmailValid(username)) {
            LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            LoginFormState(passwordError = R.string.invalid_password_login)
        } else {
            LoginFormState(isDataValid = true)
        }
    }

    fun createUserDataChanged(email: String, password: String, name: String) {
        if (!isEmailValid(email)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password_register)
        } else if (!isNameValid(name)) {
            _loginForm.value = LoginFormState(nameError = R.string.invalid_name)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }
}