package com.br.appchecker.ui.questions.adapters.viewholders

import android.view.View
import android.widget.RadioGroup
import androidx.recyclerview.widget.RecyclerView
import com.br.appchecker.R
import com.br.appchecker.data.model.Question
import com.br.appchecker.databinding.ItemSingleChoiceQuestionBinding
import com.br.appchecker.ui.questions.adapters.SingleChoiceAdapter
import com.google.android.material.radiobutton.MaterialRadioButton

class SingleChoiceViewHolder(
    binding:ItemSingleChoiceQuestionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        binding: ItemSingleChoiceQuestionBinding, question: Question,
        listener: SingleChoiceAdapter.OnItemClickListener) {
        with(binding) {
            title.text = question.title
            description.text = question.description
            answersGroup.removeAllViews()

            for (i in question.answers.indices) {
                val answer = question.answers[i]
                val radioButton = MaterialRadioButton(itemView.context).apply {
                    id = i
                    layoutDirection = View.LAYOUT_DIRECTION_RTL
                    setBackgroundResource(R.drawable.background_question)
                    setPadding(20, 20, 20, 20)
                    layoutParams = RadioGroup.LayoutParams(
                        RadioGroup.LayoutParams.MATCH_PARENT,
                        RadioGroup.LayoutParams.WRAP_CONTENT).apply {
                        setMargins(0, 16, 0, 16)
                    }
                    text = answer
                    setTextAppearance(android.R.style.TextAppearance_Holo_Small)
                    isChecked = (i == question.selectedAnswerPosition)
                    setOnClickListener {
                        question.selectedAnswerPosition = i
                        listener.onItemClick(question, question.selectedAnswerPosition!!)
                    }
                }
                answersGroup.addView(radioButton)
            }
        }
    }
}