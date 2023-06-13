package com.br.appchecker.ui.questions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.br.appchecker.data.model.Question
import com.br.appchecker.databinding.FragmentFirstBinding
import com.br.appchecker.ui.questions.adapters.SingleChoiceAdapter
import ulid.ULID

class FirstFragment : BaseFragment <FragmentFirstBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) ->
    FragmentFirstBinding get() = FragmentFirstBinding::inflate

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

    override fun getProgressBarIndex(): Int {
        return 1
    }

    override fun getProgressBarMessage(): String {
        return "1 de 6"
    }

    private fun configRecyclerView() {
        val recyclerView = binding.rv
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val questions = mutableListOf<Question>()
        questions.add(Question(
            id = ULID.randomULID(),
            title = "Seus rendimentos tributáveis foram superiores a R$ 28.559,70 no ano passado?",
            description = "Selecione a opção que te melhor descreve",
            answers = listOf(
                "Sim, acima do limite estabelecido",
                "Não, não recebi acima do limite estabelecido",
                "Não sei / Não tenho certeza",
                "Não se aplica a mim"),
            selectedAnswerPosition = null))
        val adapter = SingleChoiceAdapter(requireContext(),
            questions, object : SingleChoiceAdapter.OnItemClickListener {
            override fun onItemClick(question: Question, position: Int) {
                    Toast.makeText(requireContext(),
                    "Você clicou no $position",
                    Toast.LENGTH_LONG).show()
            }
        })
        recyclerView.adapter = adapter
    }

}