package com.br.appchecker.presentation.login.state
data class LoginFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val nameError: Int? = null,
    val isDataValid: Boolean = false
)