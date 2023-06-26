package com.br.appchecker.data.repository.login

import android.util.Log
import com.br.appchecker.data.local.dao.UserDao
import com.br.appchecker.data.remote.config.ApiServiceFactory
import com.br.appchecker.data.remote.request.LoginRequest
import com.br.appchecker.data.remote.request.UserRequest
import com.br.appchecker.data.remote.response.LoginResponse
import com.br.appchecker.data.remote.response.UserResponse
import com.br.appchecker.data.state.StateInfo
import com.br.appchecker.data.state.StateLogin
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
            userDao.deleteAll()
            val guestUser = User(email = "convidado@email.com", name = "convidado", password = "")
            if (userDao.getAll().isEmpty()) {
                userDao.insert(guestUser)
                StateLogin.Success(
                    User(guestUser.id, guestUser.email, guestUser.name, guestUser.password),
                    "Usuário autenticado com sucesso"
                )
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

    override suspend fun createUser(
        email: String,
        password: String,
        name: String
    ): StateInfo<UserResponse> {
        return try {
            val response = service.createUser(UserRequest(email, password, name)).awaitResponse()

            if (response.code() == 404) {
                return StateInfo.Error(
                    message = "O usuário já existe com este email.",
                    title = "Usuário existente"
                )
            }

            if (response.isSuccessful) {
                val newUser = response.body()
                if (newUser != null) {
                    StateInfo.Success(status = newUser.status, info = newUser.info)
                } else {
                    Log.e(null, "Erro ao criar um usuário")
                    StateInfo.Error(
                        message = "Ocorreu um erro ao criar um usuário. Resposta inválida.",
                        txt = "Por favor, tente novamente",
                        title = "Ocorreu um erro"
                    )
                }
            } else {
                Log.e(null, "Erro ao criar um usuário")
                StateInfo.Error(
                    message = "Ocorreu um erro ao criar um usuário. Código de resposta: ${response.code()}",
                    code = response.code(),
                    txt = "\n\nDeseja tentar novamente?",
                    title = "Ocorreu um erro"
                )
            }
        } catch (e: Exception) {
            Log.e("Erro ao criar um usuário", "$e")
            StateInfo.Error(
                message = "Ocorreu um erro ao criar um usuário.",
                exception = e
            )
        }
    }

    override suspend fun deleteAllUsers() {
        userDao.deleteAll()
    }

    override suspend fun logout() {
        userDao.deleteAll()
    }
}