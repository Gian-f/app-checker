package com.br.appchecker.presentation.questions.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.br.appchecker.data.local.dao.QuestionDao
import com.br.appchecker.data.local.dao.UserDao
import com.br.appchecker.data.repository.question.QuestionRepositoryImpl
import com.br.appchecker.presentation.questions.viewmodels.QuestionViewModel

class QuestionViewModelFactory(
    private val questionDao: QuestionDao,
    private val userDao: UserDao
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuestionViewModel::class.java)) {
            return QuestionViewModel(
                    repository = QuestionRepositoryImpl(questionDao, userDao)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}