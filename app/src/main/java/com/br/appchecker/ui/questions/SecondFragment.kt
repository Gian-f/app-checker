package com.br.appchecker.ui.questions

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.br.appchecker.data.model.Question
import com.br.appchecker.databinding.FragmentSecondBinding
import com.br.appchecker.ui.questions.adapters.SingleChoiceQuestionAdapter
import com.br.appchecker.ui.questions.interfaces.ProgressBarListener
import ulid.ULID

class SecondFragment : Fragment() {

    private val binding: FragmentSecondBinding by
    lazy { FragmentSecondBinding.inflate(layoutInflater) }

    private var progressBarListener: ProgressBarListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBarListener?.onUpdateProgressBar(2, "2 de 6")
        configRecyclerView()
    }


    private fun configRecyclerView() {
        val recyclerView = binding.rvSecond
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val questions = listOf<Question>().toMutableList()
        questions.add(
            Question(
            id = ULID.randomULID(),
            title = "Sua receita bruta na atividade rural foi superior a R$ 142.798,50?",
            description = "Selecione a opção que melhor descreve a sua situação",
            answers = listOf(
                "Sim, acima do limite estabelecido",
                "Não, não recebi acima do limite estabelecido",
                "Não sei / Não tenho certeza",
                "Não se aplica"),
            selectedAnswerPosition = null))
        val adapter = SingleChoiceQuestionAdapter(questions, object :
            SingleChoiceQuestionAdapter.OnItemClickListener {
            override fun onItemClick(question: Question, position: Int) {
                Toast.makeText(requireContext(), "você clicou no $position", Toast.LENGTH_SHORT).show()
            }
        })
        recyclerView.adapter = adapter
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        progressBarListener = context as? ProgressBarListener
    }
    override fun onDetach() {
        super.onDetach()
        progressBarListener = null
    }
}