package com.br.appchecker.ui.questions.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.br.appchecker.domain.model.Question
import com.br.appchecker.databinding.ItemSingleChoiceQuestionBinding
import com.br.appchecker.ui.questions.adapters.viewholders.SingleChoiceViewHolder


class SingleChoiceAdapter(
    val context: Context,
    private val listener: OnItemClickListener
) : ListAdapter<Question, SingleChoiceViewHolder>(QuestionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChoiceViewHolder {
        val binding = ItemSingleChoiceQuestionBinding.inflate(LayoutInflater.from(context), parent, false)
        return SingleChoiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SingleChoiceViewHolder, position: Int) {
        val question = getItem(position)
        holder.bind(question, listener)
    }

    /*  fun getQuestions(): List<Question> {
        return questions
    }*/

    interface OnItemClickListener {
        fun onItemClick(question: Question, position: Int)
    }

    private class QuestionDiffCallback : DiffUtil.ItemCallback<Question>() {
        override fun areItemsTheSame(oldItem: Question, newItem: Question) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Question, newItem: Question) =
            oldItem == newItem
    }
}
