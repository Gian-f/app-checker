package com.br.appchecker.ui.questions.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.br.appchecker.data.repository.QuestionRepositoryImpl
import com.br.appchecker.ui.questions.viewmodels.QuestionViewModel

class QuestionViewModelFactory (
    private val questionRepositoryImpl: QuestionRepositoryImpl
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuestionViewModel::class.java)) {
            return QuestionViewModel(questionRepositoryImpl = questionRepositoryImpl) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}