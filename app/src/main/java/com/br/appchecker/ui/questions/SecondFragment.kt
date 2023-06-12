package com.br.appchecker.ui.questions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.br.appchecker.data.model.Question
import com.br.appchecker.databinding.FragmentSecondBinding
import com.br.appchecker.ui.questions.adapters.SingleChoiceQuestionAdapter
import ulid.ULID

class SecondFragment : BaseFragment<FragmentSecondBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) ->
    FragmentSecondBinding get() = FragmentSecondBinding::inflate
    override fun getProgressBarIndex(): Int {
        return 2
    }

    override fun getProgressBarMessage(): String {
        return "2 de 6"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = bindingInflater.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configRecyclerView()
    }

    private fun configRecyclerView() {
        val recyclerView = binding.rvSecond
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val questions = mutableListOf<Question>()
        questions.add(
            Question(
                id = ULID.randomULID(),
                title = "Sua receita bruta na atividade rural foi superior a R$ 142.798,50?",
                description = "Selecione a opção que te melhor descreve",
                answers = listOf(
                    "Sim, acima do limite estabelecido",
                    "Não, não recebi acima do limite estabelecido",
                    "Não sei / Não tenho certeza",
                    "Não se aplica a mim"
                ),
                selectedAnswerPosition = null
            )
        )
        println(questions)
        val adapter = SingleChoiceQuestionAdapter(questions, object :
            SingleChoiceQuestionAdapter.OnItemClickListener {
            override fun onItemClick(question: Question, position: Int) {
                Toast.makeText(requireContext(), "você clicou no $position", Toast.LENGTH_SHORT)
                    .show()
            }
        })
        recyclerView.adapter = adapter
    }
}