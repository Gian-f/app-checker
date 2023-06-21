package com.br.appchecker.domain.usecase.question

import com.br.appchecker.data.repository.question.QuestionRepository
import com.br.appchecker.domain.model.Question

class GetQuestionsUseCase(
    private val questionRepository: QuestionRepository,
) {
    suspend fun execute(): List<Question> {
        return questionRepository.getAllQuestions()
    }
}