package com.br.appchecker.data.repository.question

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.br.appchecker.data.local.dao.QuestionDao
import com.br.appchecker.data.local.dao.UserDao
import com.br.appchecker.data.remote.config.ApiServiceFactory
import com.br.appchecker.data.remote.request.QuestionRequest
import com.br.appchecker.data.remote.response.AnswersData
import com.br.appchecker.data.remote.service.QuestionService
import com.br.appchecker.domain.model.Question
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class QuestionRepositoryImpl(
    private val questionDao: QuestionDao,
    private val userDao: UserDao,
    private val context: Context
    ) : QuestionRepository {

    private val service: QuestionService by lazy {
        ApiServiceFactory.createQuestionService(context)
    }

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

    override suspend fun getAllQuestionsFromFirebase(): List<Question> {
        val questionsRef = FirebaseDatabase.getInstance().getReference("questions")

        return suspendCoroutine { continuation ->
            questionsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val questions = mutableListOf<Question>()

                    for (questionSnapshot in dataSnapshot.children) {
                        val id = questionSnapshot.child("id").getValue(Int::class.java)
                        val title = questionSnapshot.child("title").getValue(String::class.java)
                        val description = questionSnapshot.child("description").getValue(String::class.java)
                        val answersDataList = mutableListOf<AnswersData>()

                        for (answersSnapshot in questionSnapshot.child("answers").children) {
                            val answerId = answersSnapshot.child("id").getValue(Int::class.java)
                            val answerDescription = answersSnapshot.child("description").getValue(String::class.java)
                            val answerQuestionId = answersSnapshot.child("questionId").getValue(Int::class.java)
                            val answerPositionOrder = answersSnapshot.child("positionOrder").getValue(Int::class.java)

                            val answerData = AnswersData(answerId ?: 0, answerDescription ?: "", answerQuestionId ?: 0, answerPositionOrder ?: 0)
                            answersDataList.add(answerData)
                        }

                        val question = Question(id ?: 0, title ?: "", description ?: "", answersDataList)
                        questions.add(question)
                    }

                    continuation.resume(questions)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    continuation.resumeWithException(databaseError.toException())
                }
            })
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