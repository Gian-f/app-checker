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
import com.br.appchecker.util.ErrorUtils.parseErrorMessage
import retrofit2.awaitResponse

class LoginRepositoryImpl(
    private val userDao: UserDao
) : LoginRepository {

    private var service = ApiServiceFactory.createLoginService()

    override suspend fun login(email: String, password: String): StateLogin<LoginResponse> {
        return try {
            val response = service.login(LoginRequest(email, password)).awaitResponse()

            val errorBody = response.errorBody()?.string()
            val errorMessage = errorBody?.let { parseErrorMessage(it) }
            if (response.isSuccessful) {
                val user = response.body()
                if (user != null) {
                    userDao.insert(user.user)
                    StateLogin.Success(user.user, user.info)
                } else {
                    Log.e(null, "Erro ao efetuar o login")
                    StateLogin.Error(
                        message = errorMessage
                            ?: "Ocorreu um erro ao efetuar login. Resposta inválida.",
                        txt = "Por favor, tente novamente",
                        title = "Ocorreu um erro"
                    )
                }
            } else {
                if (errorBody != null) {
                    // Erro ao criar usuário, retorne a mensagem de erro do servidor
                    StateLogin.Error(
                        message = errorMessage ?: "Ocorreu um erro ao efetuar o login",
                        title = "Ocorreu um erro"
                    )
                } else {
                    Log.e(null, "Erro ao criar um usuário")
                    StateLogin.Error(
                        message = "Ocorreu um erro ao criar um usuário ${response.code()}",
                        code = response.code(),
                        txt = "\n\nDeseja tentar novamente?",
                        title = "Ocorreu um erro"
                    )
                }
                Log.e(null, "Erro ao efetuar o login")
                StateLogin.Error(
                    message =
                    errorMessage ?: "Usuário ou senha incorreta!",
                    code = response.code(),
                    txt = "\n\nDeseja tentar novamente?",
                    title = "Ocorreu um erro"
                )
            }
        } catch (e: Exception) {
            Log.e("Erro ao efetuar o login", "$e")
            StateLogin.Error(
                message = "${e.message}",
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

            val responseBody = response.body()
            val errorBody = response.errorBody()?.string()

            if (response.isSuccessful) {
                if (responseBody != null) {
                    // Sucesso ao criar usuário, retorne os dados do usuário
                    StateInfo.Success(status = responseBody.status, info = responseBody.info)
                } else {
                    Log.e(null, "Erro ao criar um usuário")
                    StateInfo.Error(
                        message = "Ocorreu um erro ao criar um usuário. Resposta inválida.",
                        txt = "Por favor, tente novamente",
                        title = "Ocorreu um erro"
                    )
                }
            } else {
                if (errorBody != null) {
                    val errorMessage = parseErrorMessage(errorBody)
                    // Erro ao criar usuário, retorne a mensagem de erro do servidor
                    StateInfo.Error(
                        message = errorMessage,
                        title = "Ocorreu um erro"
                    )
                } else {
                    Log.e(null, "Erro ao criar um usuário")
                    StateInfo.Error(
                        message = "errorMessage ${response.code()}",
                        code = response.code(),
                        txt = "\n\nDeseja tentar novamente?",
                        title = "Ocorreu um erro"
                    )
                }
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