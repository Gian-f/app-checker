package com.br.appchecker.data.repository.login

import com.br.appchecker.data.state.StateLogin
import com.br.appchecker.data.remote.response.LoginResponse
import com.br.appchecker.data.remote.response.UserResponse
import com.br.appchecker.data.state.StateInfo

interface LoginRepository {

    suspend fun login(email: String, password: String): StateLogin<LoginResponse>
    suspend fun loginFirebase(email: String, password: String): StateLogin<LoginResponse>
    suspend fun loginAsGuest(): StateLogin<LoginResponse>
    suspend fun createUser(email: String, password: String, name: String): StateInfo<UserResponse>
    suspend fun createUserFirebase(email: String, password: String, name: String): StateInfo<UserResponse>
    suspend fun deleteAllUsers()
    suspend fun logout()

}