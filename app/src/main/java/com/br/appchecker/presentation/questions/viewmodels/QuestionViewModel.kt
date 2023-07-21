package com.br.appchecker.presentation.questions.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.appchecker.data.remote.response.AnswersData
import com.br.appchecker.data.repository.question.QuestionRepositoryImpl
import com.br.appchecker.domain.model.Question
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuestionViewModel(
    private val repository: QuestionRepositoryImpl
) : ViewModel() {

    private val _questions = MutableLiveData<List<Question>>()
    val questions: LiveData<List<Question>> get() = _questions

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question> get() = _question

    private val _questionByUserId = MutableLiveData<List<Question>>()
    val questionByUserId: LiveData<List<Question>> get() = _questionByUserId

    private val _answers = MutableLiveData<AnswersData>()
    private val answers: LiveData<AnswersData> get() = _answers

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading


    fun getAllQuestions() {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            Log.e("Erro Quest", "$throwable")
        }) {
            try {
                val result = repository.getAllQuestions()
                withContext(Dispatchers.Main) {
                    _questions.value = result
                }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    fun getAllQuestionsFromFirebase() {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            _loading.postValue(false)
            Log.e("Erro Quest", "$throwable")
        }) {
            _loading.postValue(true)
            try {
                val result = repository.getAllQuestionsFromFirebase()
                withContext(Dispatchers.Main) {
                    _questions.value = result
                }
            } catch (e: Exception) {
                throw e
            } finally {
                _loading.postValue(false)
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
                val result = repository.insertQuestion()
                _question.value = result
            } catch (e: Exception) {
                throw e
            }
        }
    }
}