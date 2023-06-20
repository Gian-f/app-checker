package com.br.appchecker.ui.questions.fragments

import androidx.lifecycle.MutableLiveData
import com.br.appchecker.domain.model.Question

object GlobalData {

    val questions = MutableLiveData<List<Question>>(listOf())
}