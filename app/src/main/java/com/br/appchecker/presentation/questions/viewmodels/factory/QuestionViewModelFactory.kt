package com.br.appchecker.presentation.questions.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.br.appchecker.domain.usecase.question.GetAnswersUseCase
import com.br.appchecker.domain.usecase.question.GetQuestionsUseCase
import com.br.appchecker.domain.usecase.question.InsertQuestionUseCase
import com.br.appchecker.presentation.questions.viewmodels.QuestionViewModel

class QuestionViewModelFactory(
    private val getQuestionsUseCase: GetQuestionsUseCase,
    private val insertQuestionUseCase: InsertQuestionUseCase,
    private val getAnswersUseCase: GetAnswersUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuestionViewModel::class.java)) {
            return QuestionViewModel(
                getQuestionsUseCase,
                insertQuestionUseCase,
                getAnswersUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}