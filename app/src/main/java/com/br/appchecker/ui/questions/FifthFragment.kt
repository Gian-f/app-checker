package com.br.appchecker.ui.questions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.br.appchecker.data.model.Question
import com.br.appchecker.databinding.FragmentFifthBinding
import com.br.appchecker.ui.questions.adapters.SingleChoiceQuestionAdapter
import ulid.ULID

class FifthFragment: BaseFragment<FragmentFifthBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) ->
    FragmentFifthBinding get() = FragmentFifthBinding::inflate
    override fun getProgressBarIndex(): Int {
        return 5
    }

    override fun getProgressBarMessage(): String {
        return "5 de 6"
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
        val recyclerView = binding.rvFifth
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val questions = mutableListOf<Question>()
        questions.add(
            Question(
                id = ULID.randomULID(),
                title = "Você fez operações em bolsas de valores, de mercadorias, de futuros e assemelhadas acima de R$40.000,00 ou com ganhos líquidos sujeitos a impostos?",
                description = "Selecione a opção que te melhor descreve",
                answers = listOf(
                    "Sim, realizei operações acima do limite estabelecido",
                    "Não, não realizei operações acima do limite estabelecido",
                    "Não sei / Não tenho certeza",
                    "Não se aplica a mim"),
                selectedAnswerPosition = null)
        )
        val adapter = SingleChoiceQuestionAdapter(questions, object :
            SingleChoiceQuestionAdapter.OnItemClickListener {
            override fun onItemClick(question: Question, position: Int) {
                Toast.makeText(requireContext(),
                    "você clicou no $position",
                    Toast.LENGTH_SHORT).show()
                println(question)
                println(questions)
            }
        })
        recyclerView.adapter = adapter
    }
}