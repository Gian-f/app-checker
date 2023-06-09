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
import com.br.appchecker.databinding.FragmentThirdBinding
import com.br.appchecker.ui.questions.adapters.SingleChoiceQuestionAdapter
import com.br.appchecker.ui.questions.interfaces.ProgressBarListener
import ulid.ULID

class ThirdFragment(override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) ->
FragmentThirdBinding) : BaseFragment<FragmentThirdBinding>() {

    override fun getProgressBarIndex(): Int {
        return 3
    }

    override fun getProgressBarMessage(): String {
        return "3 de 6"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configRecyclerView()
    }

    private fun configRecyclerView() {
        val recyclerView = binding.rvThird
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val questions = mutableListOf<Question>()
        questions.add(
            Question(
            id = ULID.randomULID(),
            title = "Você tinha a posse ou propriedade, em 31 de dezembro do ano-calendário, de bens ou direitos (incluindo terra nua) acima de R$ 300.000,00?",
            description = "Selecione a opção que te melhor descreve",
            answers = listOf(
                "Sim, acima do limite estabelecido",
                "Não, eu tinha acima do limite estabelecido",
                "Não sei / Não tenho certeza",
                "Não se aplica"),
            selectedAnswerPosition = null)
        )
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