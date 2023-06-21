package com.br.appchecker.data.repository.login

import android.util.Log
import com.br.appchecker.data.local.AppDatabase
import com.br.appchecker.data.local.dao.UserDao
import com.br.appchecker.data.state.StateLogin
import com.br.appchecker.data.remote.config.ApiServiceFactory
import com.br.appchecker.data.remote.request.LoginRequest
import com.br.appchecker.data.remote.response.LoginResponse
import retrofit2.awaitResponse

class LoginRepositoryImpl(
    private val userDao: UserDao
    ) : LoginRepository {

    private var service = ApiServiceFactory.createLoginService()

    override suspend fun login(email: String, password: String): StateLogin<LoginResponse> {
        return try {
            val response = service.login(LoginRequest(email, password)).awaitResponse()
            if (response.isSuccessful) {
                val user = response.body()
                if (user != null) {
                    userDao.insert(user.user)
                    StateLogin.Success(user.user, user.info)
                } else {
                    Log.e(null, "Erro ao efetuar o login")
                    StateLogin.Error(
                        message = "Ocorreu um erro ao efetuar login. Resposta inválida.",
                        txt = "Por favor, tente novamente",
                        title = "Ocorreu um erro"
                    )
                }
            } else {
                Log.e(null, "Erro ao efetuar o login")
                StateLogin.Error(
                    message =
                    "Ocorreu um erro ao efetuar login. Código de resposta: ${response.code()}",
                    code = response.code(),
                    txt = "\n\nDeseja tentar novamente?",
                    title = "Ocorreu um erro"
                )
            }
        } catch (e: Exception) {
            Log.e("Erro ao efetuar o login", "$e")
            StateLogin.Error(
                message = "Ocorreu um erro ao efetuar login.",
                exception = e
            )
        }
    }

    suspend fun deleteAllUsers() {
        userDao.deleteAll()
    }

    override suspend fun logout() {
        userDao.deleteAll()
    }
}