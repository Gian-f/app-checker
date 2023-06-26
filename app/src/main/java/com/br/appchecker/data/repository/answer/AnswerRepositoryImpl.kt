package com.br.appchecker.data.repository.answer

import android.util.Log
import com.br.appchecker.data.remote.config.ApiServiceFactory
import com.br.appchecker.data.remote.request.AnswerRequest
import com.br.appchecker.data.state.StateInfo
import retrofit2.awaitResponse

class AnswerRepositoryImpl : AnswerRepository {

    private var service = ApiServiceFactory.createAnswerService()

    override suspend fun insertAnswer(
        questionOption: Int,
        questionUser: Int,
        questionCode: Int
    ): StateInfo<AnswerRequest> {
        return try {
            val response =
                service.createAnswer(AnswerRequest(questionCode, questionOption, questionUser))
                    .awaitResponse()
            if (response.isSuccessful) {
                val answer = response.body()
                if (answer != null) {
                    StateInfo.Success(answer.answers, answer.info)
                } else {
                    Log.e(null, "Erro ao efetuar o login")
                    StateInfo.Error(
                        message = "Ocorreu um erro ao efetuar login. Resposta inválida.",
                        txt = "Por favor, tente novamente",
                        title = "Ocorreu um erro"
                    )
                }
            } else {
                Log.e(null, "Erro ao efetuar o login")
                StateInfo.Error(
                    message =
                    "Ocorreu um erro ao efetuar login. Código de resposta: ${response.code()}",
                    code = response.code(),
                    txt = "\n\nDeseja tentar novamente?",
                    title = "Ocorreu um erro"
                )
            }
        } catch (e: Exception) {
            Log.e("Erro ao efetuar o login", "$e")
            StateInfo.Error(
                message = "Ocorreu um erro ao efetuar login.",
                exception = e
            )
        }
    }
}