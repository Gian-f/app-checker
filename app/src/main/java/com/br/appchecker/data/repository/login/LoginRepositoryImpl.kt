package com.br.appchecker.data.repository.login

import com.br.appchecker.data.model.login.StateLogin
import com.br.appchecker.data.remote.config.ApiServiceFactory
import com.br.appchecker.data.remote.request.LoginRequest
import com.br.appchecker.data.remote.response.LoginResponse
import retrofit2.awaitResponse

class LoginRepositoryImpl : LoginRepository {

    private var service = ApiServiceFactory.createLoginService()

    override suspend fun login(email: String, password: String): StateLogin<LoginResponse> {
        return try {
            val response = service.login(LoginRequest(email, password)).awaitResponse()
            if (response.isSuccessful) {
                val user = response.body()
                if (user != null) {
                    StateLogin.Success(user, "Login efetuado com sucesso!")
                } else {
                    StateLogin.Error(
                        message = "Ocorreu um erro ao efetuar login. Resposta inválida.",
                        txt = "",
                        title = "Ocorreu um erro"
                    )
                }
            } else {
                StateLogin.Error(
                    message =
                    "Ocorreu um erro ao efetuar login. Código de resposta: ${response.code()}",
                    code = response.code(),
                    txt = "\n\nDeseja tentar novamente?",
                    title = "Ocorreu um erro"
                )
            }
        } catch (e: Exception) {
            StateLogin.Error(
                message = "Ocorreu um erro ao efetuar login.",
                exception = e
            )
        }
    }


    override suspend fun logout() {
        TODO()
    }
}