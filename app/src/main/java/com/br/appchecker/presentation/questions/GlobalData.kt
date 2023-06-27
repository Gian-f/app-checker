package com.br.appchecker.presentation.questions

import androidx.lifecycle.MutableLiveData
import com.br.appchecker.domain.model.Question

object GlobalData {
    val globalQuestions = MutableLiveData<List<Question>>(listOf())
}