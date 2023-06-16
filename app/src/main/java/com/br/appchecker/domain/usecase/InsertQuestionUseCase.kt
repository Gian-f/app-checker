package com.br.appchecker.domain.usecase

import com.br.appchecker.data.model.Question
import com.br.appchecker.data.repository.QuestionRepository


class InsertQuestionUseCase(
    private val questionRepository: QuestionRepository
) {
    suspend fun execute(): Question {
        return questionRepository.insertQuestion()
    }
}