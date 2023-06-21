package com.br.appchecker.data.repository.question

import androidx.recyclerview.widget.RecyclerView
import com.br.appchecker.data.local.dao.QuestionDao
import com.br.appchecker.data.local.dao.UserDao
import com.br.appchecker.data.remote.config.ApiServiceFactory
import com.br.appchecker.data.remote.request.QuestionRequest
import com.br.appchecker.domain.model.Question

class QuestionRepositoryImpl(
    private val questionDao: QuestionDao,
    private val userDao: UserDao
    ) : QuestionRepository {

    private var service = ApiServiceFactory.createQuestionService()

    override suspend fun getAllQuestions(): List<Question> {
        val user = userDao.find()
        return service.getAllQuestions(QuestionRequest(
            codigoUsuario = user?.id ?: 0,
            limite = 5
        )).map { response ->
            Question(
                id = response.question.id,
                description = response.question.description,
                title = response.question.title,
                answers = response.answers,
            )
        }
    }

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