package com.br.appchecker.domain.usecase.question

import com.br.appchecker.domain.model.Question
import com.br.appchecker.data.remote.request.QuestionRequest
import com.br.appchecker.data.repository.question.QuestionRepository

class GetQuestionsUseCase(
    private val questionRepository: QuestionRepository
) {
    suspend fun execute(questionRequest: QuestionRequest): List<Question> {
        return questionRepository.getAllQuestions(questionRequest)
    }
}