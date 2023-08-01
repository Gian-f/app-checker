package com.br.appchecker.presentation.questions.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.br.appchecker.R
import com.br.appchecker.databinding.ItemSingleChoiceQuestionBinding
import com.br.appchecker.domain.model.Question
import com.br.appchecker.presentation.questions.adapters.viewholders.SingleChoiceViewHolder
import com.br.appchecker.util.enums.EnumDescription

class SingleChoiceAdapter(
    val context: Context,
    private val listener: OnItemClickListener,
) : ListAdapter<Question, SingleChoiceViewHolder>(QuestionDiffCallback()) {

    private val descriptionToImageMap = mapOf(
        EnumDescription.RENDA.value to R.drawable.ic_money,
        EnumDescription.RURAL.value to R.drawable.ic_agriculture,
        EnumDescription.BENS.value to R.drawable.ic_business,
        EnumDescription.IMOVEL.value to R.drawable.ic_home,
        EnumDescription.BOLSA.value to R.drawable.ic_currency_exchange,
        EnumDescription.VIAGEM.value to R.drawable.ic_airplane
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChoiceViewHolder {
        val binding = ItemSingleChoiceQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SingleChoiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SingleChoiceViewHolder, position: Int) {
        val question = getItem(position)
        setupImageDescription(holder.binding, question)
        holder.bind(question, listener)
    }

    private fun setupImageDescription(binding: ItemSingleChoiceQuestionBinding, question: Question) {
        val imageResource = descriptionToImageMap[question.description]
        if (imageResource != null) {
            binding.ivDescription.setImageResource(imageResource)
        } else {
            binding.ivDescription.setImageDrawable(null)
        }
    }

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
