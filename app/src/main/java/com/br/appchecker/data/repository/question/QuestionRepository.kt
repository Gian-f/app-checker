package com.br.appchecker.data.repository.question

import com.br.appchecker.domain.model.Question
import com.br.appchecker.data.remote.request.QuestionRequest

interface QuestionRepository {
    suspend fun getAllQuestions(questionRequest: QuestionRequest): List<Question>

//    suspend fun getAllQuestionsById(idUser: Int): List<Question>

    suspend fun insertQuestion(): Question
}