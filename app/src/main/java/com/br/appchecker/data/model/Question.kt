package com.br.appchecker.data.model

import androidx.recyclerview.widget.RecyclerView

data class Question(
    val id: String,
    val title: String,
    val description: String,
    val answers: List<String>,
    var selectedAnswerPosition: Int = RecyclerView.NO_POSITION
)
