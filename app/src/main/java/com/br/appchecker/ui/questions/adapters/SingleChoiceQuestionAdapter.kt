package com.br.appchecker.ui.questions.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.br.appchecker.R
import com.br.appchecker.data.model.Question
import com.google.android.material.radiobutton.MaterialRadioButton

class SingleChoiceQuestionAdapter(
    private val questions: MutableList<Question>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<SingleChoiceQuestionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_single_choice_question, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val question = questions[position]
        holder.bind(question, listener)
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.title)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.description)
        private val radioGroup: RadioGroup = itemView.findViewById(R.id.answers_group)

        fun bind(question: Question, listener: OnItemClickListener) {
            titleTextView.text = question.title
            descriptionTextView.text = question.description
            radioGroup.removeAllViews()
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
                radioGroup.addView(radioButton)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(question: Question, position: Int)
    }
}