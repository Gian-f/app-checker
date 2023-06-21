package com.br.appchecker.data.repository.question

import com.br.appchecker.domain.model.Question

interface QuestionRepository {
    suspend fun getAllQuestions(): List<Question>
    suspend fun insertQuestion(): Question
}