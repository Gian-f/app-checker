package com.br.appchecker.presentation.questions.adapters

import androidx.recyclerview.widget.DiffUtil
import com.br.appchecker.domain.model.Question

object QuestionDiffCallback : DiffUtil.ItemCallback<Question>() {
    override fun areItemsTheSame(oldItem: Question, newItem: Question) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Question, newItem: Question) =
        oldItem == newItem
}
