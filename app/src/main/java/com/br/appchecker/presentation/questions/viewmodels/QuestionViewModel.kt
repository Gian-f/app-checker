package com.br.appchecker.presentation.questions.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.appchecker.data.remote.response.AnswersData
import com.br.appchecker.domain.model.Question
import com.br.appchecker.domain.usecase.question.GetAnswersUseCase
import com.br.appchecker.domain.usecase.question.GetQuestionsUseCase
import com.br.appchecker.domain.usecase.question.InsertQuestionUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuestionViewModel(
    private val getQuestionsUseCase: GetQuestionsUseCase,
    private val insertQuestionUseCase: InsertQuestionUseCase,
    private val getAnswersUseCase: GetAnswersUseCase
) : ViewModel() {

    private val _questions = MutableLiveData<List<Question>>()
    val questions: LiveData<List<Question>> get() = _questions

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question> get() = _question

    private val _questionByUserId = MutableLiveData<List<Question>>()
    val questionByUserId: LiveData<List<Question>> get() = _questionByUserId

    private val _answers = MutableLiveData<AnswersData>()
    private val answers: LiveData<AnswersData> get() = _answers

    fun getAllQuestions() {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            Log.e("Erro Quest", "$throwable")
        }) {
            try {
                val result = getQuestionsUseCase.execute()
                withContext(Dispatchers.Main) {
                    _questions.value = result
                }
            } catch (e: Exception) {
                throw e
            }
        }
    }

//    fun getAllAnswers() {
//        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
//            Log.e("Erro Answers", "$throwable")
//        }) {
//            try {
//                val result = TODO()
//                withContext(Dispatchers.Main) {
//                    _answers.value = result
//                }
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