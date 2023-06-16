package com.br.appchecker.data.repository

import com.br.appchecker.data.model.Question
import com.br.appchecker.data.remote.config.QuestionServiceFactory

class QuestionRepositoryImpl : QuestionRepository {

    private var service = QuestionServiceFactory.createQuestionService()
    override suspend fun getAllQuestions(): List<Question> {
        return service.getQuestions().map {
            Question(
                id = it.id.toString(),
                description = it.description,
                title = it.title,
                answers = listOf())
        }
    }

    override suspend fun insertQuestion(): Question {
        return with(service.insertQuestion()) {
            Question(
                id = id.toString(),
                description = description,
                title = title,
                answers = listOf())
        }
    }
}