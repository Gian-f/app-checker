package com.br.appchecker.ui.questions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.br.appchecker.data.model.Question
import com.br.appchecker.databinding.FragmentThirdBinding
import com.br.appchecker.ui.questions.adapters.SingleChoiceAdapter
import ulid.ULID

class ThirdFragment : BaseFragment<FragmentThirdBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) ->
    FragmentThirdBinding
        get() = FragmentThirdBinding::inflate
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
        binding = bindingInflater.invoke(inflater, container, false)
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
            title = "Você obteve ganho de capital na venda de bens ou direitos sujeito ao imposto?",
            description = "Selecione a opção que te melhor descreve",
            answers = listOf(
                "Sim, obtive ganho de capital em venda de bens",
                "Não, não obtive ganho de capital em venda de bens",
                "Não sei / Não tenho certeza",
                "Não se aplica a mim"),
            selectedAnswerPosition = null)
        )
        val adapter = SingleChoiceAdapter(requireContext(),questions, object :
            SingleChoiceAdapter.OnItemClickListener {
            override fun onItemClick(question: Question, position: Int) {
                Toast.makeText(requireContext(), "você clicou no $position", Toast.LENGTH_SHORT).show()
            }
        })
        recyclerView.adapter = adapter
    }
}