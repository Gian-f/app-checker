package com.br.appchecker.ui.questions.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.br.appchecker.data.model.Question
import com.br.appchecker.data.repository.QuestionRepositoryImpl
import kotlinx.coroutines.launch

class QuestionViewModel(
    private val questionRepositoryImpl: QuestionRepositoryImpl,
): ViewModel() {

    private val _questions = MutableLiveData<List<Question>>()
    val questions: LiveData<List<Question>> get() = _questions

    private val _question = MutableLiveData<Question>()
    private val question: LiveData<Question> get() = _question

    fun getAllQuestions() {
        viewModelScope.launch {
            try {
                val result = questionRepositoryImpl.getAllQuestions()
                _questions.value = result
            } catch (e: Exception) {
                throw e
            }
        }
    }

    fun insertQuestion(question: Question) {
        viewModelScope.launch {
            try {
                val result = questionRepositoryImpl.insertQuestion()
                _question.value = result
            } catch (e: Exception) {
                throw e
            }
        }
    }
}