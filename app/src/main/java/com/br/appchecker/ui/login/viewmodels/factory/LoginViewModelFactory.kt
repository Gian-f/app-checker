package com.br.appchecker.ui.login.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.br.appchecker.data.local.dao.UserDao
import com.br.appchecker.data.repository.login.LoginRepositoryImpl
import com.br.appchecker.ui.login.viewmodels.LoginViewModel

class LoginViewModelFactory(
    private val userDao: UserDao
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                loginRepository = LoginRepositoryImpl(userDao),

            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}