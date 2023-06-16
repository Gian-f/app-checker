package com.br.appchecker.data.repository

import com.br.appchecker.data.model.Question

interface QuestionRepository {
    suspend fun getAllQuestions(): List<Question>
    suspend fun insertQuestion(): Question
}