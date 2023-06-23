package com.br.appchecker.ui.login.state
data class LoginFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val nameError: Int? = null,
    val isDataValid: Boolean = false
)