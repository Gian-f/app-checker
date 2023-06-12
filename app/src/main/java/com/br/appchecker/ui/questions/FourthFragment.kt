package com.br.appchecker.ui.questions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.br.appchecker.data.model.Question
import com.br.appchecker.databinding.FragmentFourthBinding
import com.br.appchecker.ui.questions.adapters.SingleChoiceQuestionAdapter
import ulid.ULID

class FourthFragment : BaseFragment<FragmentFourthBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) ->
    FragmentFourthBinding get() = FragmentFourthBinding::inflate
    override fun getProgressBarIndex(): Int {
        return 4
    }

    override fun getProgressBarMessage(): String {
        return "4 de 6"
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
        val recyclerView = binding.rvFourth
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val questions = mutableListOf<Question>()
        questions.add(
            Question(
                id = ULID.randomULID(),
                title = "Você tinha a posse ou propriedade, em 31 de dezembro do ano-calendário, de bens ou direitos (incluindo terra nua) acima de R$ 300.000,00?",
                description = "Selecione a opção que te melhor descreve",
                answers = listOf(
                    "Sim, possuo posse ou propriedade acima do limite estabelecido",
                    "Não, não possuo posse ou propriedade acima do limite estabelecido",
                    "Não sei / Não tenho certeza",
                    "Não se aplica a mim"),
                selectedAnswerPosition = null))
        println(questions)
        val adapter = SingleChoiceQuestionAdapter(questions, object :
            SingleChoiceQuestionAdapter.OnItemClickListener {
            override fun onItemClick(question: Question, position: Int) {
                Toast.makeText(requireContext(), "você clicou no $position", Toast.LENGTH_SHORT).show()
            }
        })
        recyclerView.adapter = adapter
    }
}