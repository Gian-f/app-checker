package com.br.appchecker.ui.questions.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.appchecker.domain.model.Question
import com.br.appchecker.data.remote.request.QuestionRequest
import com.br.appchecker.domain.usecase.question.GetQuestionsUseCase
import com.br.appchecker.domain.usecase.question.InsertQuestionUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuestionViewModel(
    private val getQuestionsUseCase: GetQuestionsUseCase,
    private val insertQuestionUseCase: InsertQuestionUseCase
) : ViewModel() {

    private val _questions = MutableLiveData<List<Question>>()
    val questions: LiveData<List<Question>> get() = _questions

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question> get() = _question

    private val _questionByUserId = MutableLiveData<List<Unit>>()
    val questionByUserId: LiveData<List<Unit>> get() = _questionByUserId

    fun getAllQuestions(questionRequest: QuestionRequest) {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            Log.e("Erro Quest", "$throwable")
        }) {
            try {
                val result = getQuestionsUseCase.execute(questionRequest)
                withContext(Dispatchers.Main) {
                    _questions.value = result
                }
            } catch (e: Exception) {
                throw e
            }
        }
    }

//    fun getAllQuestionsByUserId(userId: Int) {
//        viewModelScope.launch ( Dispatchers.IO + CoroutineExceptionHandler { _, throwable -> Log.e("Erro Quest", "$throwable") }) {
//            try {
//                val result = getQuestionsByUserIdUseCase.execute(userId)
//                _questionByUserId.value = listOf(result)
//            } catch (e: Exception) {
//                throw e
//            }
//        }
//    }

    fun insertQuestion(question: Question) {
        viewModelScope.launch {
            try {
                val result = insertQuestionUseCase.execute()
                _question.value = result
            } catch (e: Exception) {
                throw e
            }
        }
    }
}