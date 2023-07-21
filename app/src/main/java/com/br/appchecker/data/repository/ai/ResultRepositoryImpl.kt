package com.br.appchecker.data.repository.ai

import android.content.Context
import android.util.Log
import com.br.appchecker.data.remote.config.ApiServiceFactory
import com.br.appchecker.data.remote.request.ChatRequest
import com.br.appchecker.data.remote.service.ResultService
import com.br.appchecker.data.state.StateInfo
import retrofit2.awaitResponse

class ResultRepositoryImpl(
    private val context: Context
) : ResultRepository {

    private val service: ResultService by lazy {
        ApiServiceFactory.createResultService(context)
    }

    override suspend fun sendMessage(message: String): StateInfo<String> {
        return try {
            val response = service.sendMessage(ChatRequest(message)).awaitResponse()

            if (response.isSuccessful) {
                val chatResponse = response.body()
                chatResponse?.message?.let {
                    StateInfo.Success(it)
                } ?: run {
                    Log.e("Chat Error", "Erro ao receber a resposta do chat. Resposta inválida.")
                    StateInfo.Error(
                        message = "Ocorreu um erro ao receber a resposta do chat. Resposta inválida.",
                        txt = "Por favor, tente novamente",
                        title = "Ocorreu um erro"
                    )
                }
            } else {
                Log.e("Chat Error", "Erro ao enviar mensagem para o chat. Código: ${response.code()}")
                StateInfo.Error(
                    message = "Ocorreu um erro ao enviar mensagem para o chat. Código: ${response.code()}",
                    code = response.code(),
                    txt = "\n\nDeseja tentar novamente?",
                    title = "Ocorreu um erro"
                )
            }
        } catch (e: Exception) {
            Log.e("Chat Error", "Erro na comunicação com o chat: $e")
            StateInfo.Error(
                message = "Ocorreu um erro na comunicação com o chat.",
                exception = e
            )
        }
    }

    override suspend fun receiveMessage(message: String): StateInfo<String> {
        return try {
            val response = service.receiveMessage(ChatRequest(message))
                .awaitResponse()

            if (response.isSuccessful) {
                val chatResponse = response.body()
                chatResponse?.message?.let {
                    StateInfo.Success(it)
                } ?: run {
                    Log.e("Chat Error", "Erro ao receber a resposta do chat. Resposta inválida.")
                    StateInfo.Error(
                        message = "Ocorreu um erro ao receber a resposta do chat. Resposta inválida.",
                        txt = "Por favor, tente novamente",
                        title = "Ocorreu um erro"
                    )
                }
            } else {
                Log.e("Chat Error", "Erro ao receber mensagem do chat. Código: ${response.code()}")
                StateInfo.Error(
                    message = "Ocorreu um erro ao receber mensagem do chat. Código: ${response.code()}",
                    code = response.code(),
                    txt = "\n\nDeseja tentar novamente?",
                    title = "Ocorreu um erro"
                )
            }
        } catch (e: Exception) {
            Log.e("Chat Error", "Erro na comunicação com o chat: $e")
            StateInfo.Error(
                message = "Ocorreu um erro na comunicação com o chat.",
                exception = e
            )
        }
    }
}
