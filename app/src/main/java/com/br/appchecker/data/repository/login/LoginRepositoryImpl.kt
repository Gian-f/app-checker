package com.br.appchecker.data.repository.login

import android.util.Log
import com.br.appchecker.data.local.AppDatabase
import com.br.appchecker.data.local.dao.UserDao
import com.br.appchecker.data.state.StateLogin
import com.br.appchecker.data.remote.config.ApiServiceFactory
import com.br.appchecker.data.remote.request.LoginRequest
import com.br.appchecker.data.remote.response.LoginResponse
import com.br.appchecker.domain.model.User
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

    override suspend fun loginAsGuest(): StateLogin<LoginResponse> {
        return try {
            val guestUser = userDao.getAll().first()
            if (guestUser.id == 0) {
                StateLogin.Success(User(0,"Convidado", "Convidado"), "Usuário autenticado com sucesso")
            } else {
                StateLogin.Error(
                    message = "O usuário não é um convidado.",
                    txt = "Por favor, faça o login regularmente."
                )
            }
        } catch (e: Exception) {
            Log.e("Erro ao efetuar o login", "$e")
            StateLogin.Error(
                message = "Ocorreu um erro ao efetuar login como usuário convidado.",
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