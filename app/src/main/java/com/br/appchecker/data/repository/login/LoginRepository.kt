package com.br.appchecker.data.repository.login

import com.br.appchecker.data.state.StateLogin
import com.br.appchecker.data.remote.response.LoginResponse

interface LoginRepository {

    suspend fun login(email: String, password: String): StateLogin<LoginResponse>

    suspend fun loginAsGuest(): StateLogin<LoginResponse>

    suspend fun logout()

}