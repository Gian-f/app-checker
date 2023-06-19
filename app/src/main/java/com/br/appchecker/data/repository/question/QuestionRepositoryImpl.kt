package com.br.appchecker.data.repository.question

import androidx.recyclerview.widget.RecyclerView
import com.br.appchecker.data.model.Question
import com.br.appchecker.data.remote.config.ApiServiceFactory

class QuestionRepositoryImpl : QuestionRepository {

    private var service = ApiServiceFactory.createQuestionService()

    override suspend fun getAllQuestions(): List<Question> {
        return service.getAllQuestions().map { response ->
            Question(
                id = response.question.id.toString(),
                description = response.question.description,
                title = response.question.title,
                answers = response.answers,
            )
        }
    }

    override suspend fun insertQuestion(): Question {
        return with(service.insertQuestion()) {
            Question(
                id = question.id.toString(),
                description = question.description,
                title = question.title,
                answers = listOf(),
                selectedAnswerPosition = RecyclerView.NO_POSITION
            )
        }
    }
}