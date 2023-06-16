package com.br.appchecker.data.repository

import com.br.appchecker.data.model.Question
import com.br.appchecker.data.remote.service.QuestionService

class QuestionRepositoryImpl(
    private val service: QuestionService
): QuestionRepository {
    override suspend fun getAllQuestions(): List<Question> {
        return service.getAll().map {
            Question(
                id = it.id.toString(),
                description = it.description,
                title = it.title,
                answers = listOf())
        }
    }

    override suspend fun insertQuestion(): Question {
        return with(service.insert()) {
            Question(
                id = id.toString(),
                description = description,
                title = title,
                answers = listOf())
        }
    }
}