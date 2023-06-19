package com.br.appchecker.data.model

import androidx.recyclerview.widget.RecyclerView
import com.br.appchecker.data.remote.response.AnswersData

data class Question(
    val id: String,
    val title: String,
    val description: String,
    val answers: List<AnswersData>,
    var selectedAnswerPosition: Int? = RecyclerView.NO_POSITION
)

