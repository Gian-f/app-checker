package com.br.appchecker.data.repository.question

import androidx.recyclerview.widget.RecyclerView
import com.br.appchecker.domain.model.Question
import com.br.appchecker.data.remote.config.ApiServiceFactory
import com.br.appchecker.data.remote.request.QuestionRequest

class QuestionRepositoryImpl : QuestionRepository {

    private var service = ApiServiceFactory.createQuestionService()

    override suspend fun getAllQuestions(questionRequest: QuestionRequest): List<Question> {
        return service.getAllQuestions(questionRequest).map { response ->
            Question(
                id = response.question.id,
                description = response.question.description,
                title = response.question.title,
                answers = response.answers,
            )
        }
    }

//    override suspend fun getAllQuestionsById(idUser: Int): List<Question> {
//        return service.getAllQuestions(idUser).map { response ->
//            Question(
//                id = response.question.id,
//                description = response.question.description,
//                title = response.question.title,
//                answers = response.answers
//            )
//        }
//    }

    override suspend fun insertQuestion(): Question {
        return with(service.insertQuestion()) {
            Question(
                id = question.id,
                description = question.description,
                title = question.title,
                answers = listOf(),
                selectedAnswerPosition = RecyclerView.NO_POSITION
            )
        }
    }
}