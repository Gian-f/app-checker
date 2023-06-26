package com.br.appchecker.domain.usecase.question

import com.br.appchecker.data.repository.answer.AnswerRepositoryImpl

class GetAnswersUseCase(
    private val repository: AnswerRepositoryImpl
) {
    suspend fun execute(questionOption: Int, questionUser: Int, questionCode: Int) {
        repository.insertAnswer(questionCode, questionUser, questionOption)
    }
}