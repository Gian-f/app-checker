package com.br.appchecker.domain.usecase

import com.br.appchecker.data.model.Question
import com.br.appchecker.data.repository.QuestionRepository

class GetQuestionsUseCase(
    private val questionRepository: QuestionRepository
) {
    suspend fun execute(): List<Question> {
        return questionRepository.getAllQuestions()
    }
}