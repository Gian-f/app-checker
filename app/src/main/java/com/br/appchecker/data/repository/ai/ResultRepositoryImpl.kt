package com.br.appchecker.data.repository.ai

import android.content.Context
import android.util.Log
import com.br.appchecker.data.remote.config.ApiServiceFactory
import com.br.appchecker.data.remote.request.ChatRequest
import com.br.appchecker.data.remote.service.ResultService
import com.br.appchecker.data.state.StateInfo
import com.br.appchecker.domain.model.Question

class ResultRepositoryImpl(
    private val context: Context
) : ResultRepository {

    private val service: ResultService by lazy {
        ApiServiceFactory.createResultService(context)
    }

    override suspend fun sendMessage(message: String, questions: List<Question>): StateInfo<String> {
        val prompt = createPromptFromMessage(message, questions)

        val gpt3Request = ChatRequest(prompt)
        return try {
            val response = service.sendMessage(gpt3Request).execute()
            if (response.isSuccessful) {
                val gpt3Response = response.body()
                val answer = gpt3Response?.choices?.get(0)?.text ?: "No response from GPT-3"
                StateInfo.Success(answer)
            } else {
                Log.e("Chat Error", "Exception in sendMessage: ${response.code()} ${response.message()}")
                StateInfo.Error("Error: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("Chat Error", "Exception in sendMessage: ${e.message}", e)
            StateInfo.Error("Error: ${e.message}")
        }
    }

    private fun createPromptFromMessage(message: String, questions: List<Question>): String {
        // Adicione a mensagem à parte inicial do prompt, se necessário.
        val promptBuilder = StringBuilder("\"mensagem\": \"$message\",\n")

        // Converta a lista de perguntas em um prompt JSON e adicione-o ao promptBuilder.
        val questionsPrompt = convertQuestionsToPrompt(questions)
        promptBuilder.append(questionsPrompt)

        // Adicione outras informações relevantes, se necessário.
        // Por exemplo, você pode adicionar informações de contexto ou outros dados relevantes ao prompt.

        // Adicione quaisquer outras informações adicionais conforme necessário.

        // Remova a vírgula extra no final e adicione as chaves para formar o prompt completo.
        val prompt = promptBuilder.toString().trimEnd().removeSuffix(",\n")
        return "{\n$prompt\n}"
    }

    private fun convertQuestionsToPrompt(questions: List<Question>): String {
        val promptBuilder = StringBuilder()

        // Percorra a lista de perguntas e adicione cada pergunta e resposta ao prompt no formato desejado.
        questions.forEachIndexed { _, question ->
            val questionKey = question.title + 1
            val answer = question.answers[question.selectedAnswerPosition!!]
            promptBuilder.append("\"$questionKey\": \"$answer\",\n")
        }

        // Remova a vírgula extra no final e adicione as chaves para formar o prompt completo.
        return promptBuilder.toString().trimEnd().removeSuffix(",\n")
    }
}
