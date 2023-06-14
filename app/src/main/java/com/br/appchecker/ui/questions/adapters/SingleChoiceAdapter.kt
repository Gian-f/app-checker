package com.br.appchecker.ui.questions.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.br.appchecker.data.model.Question
import com.br.appchecker.databinding.ItemSingleChoiceQuestionBinding
import com.br.appchecker.ui.questions.adapters.viewholders.SingleChoiceViewHolder

class SingleChoiceAdapter(
    val context: Context,
    private val questions: MutableList<Question>,
    private val listener: OnItemClickListener
    ) : RecyclerView.Adapter<SingleChoiceViewHolder>() {

    val binding : ItemSingleChoiceQuestionBinding by
     lazy { ItemSingleChoiceQuestionBinding.inflate(LayoutInflater.from(context)) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChoiceViewHolder {
        return SingleChoiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SingleChoiceViewHolder, position: Int) {
        val question = questions[position]
        holder.bind(binding, question, listener)
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    fun getQuestions(): List<Question> {
        return questions
    }

    interface OnItemClickListener {
        fun onItemClick(question: Question, position: Int)
    }
}