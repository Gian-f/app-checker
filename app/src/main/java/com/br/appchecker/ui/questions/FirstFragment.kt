package com.br.appchecker.ui.questions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.br.appchecker.R
import com.br.appchecker.data.model.Question
import com.br.appchecker.databinding.FragmentFirstBinding
import com.br.appchecker.ui.questions.adapters.SingleChoiceAdapter
import com.br.appchecker.util.showBottomSheet
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
        setupListeners()
    }

    private fun setupListeners() {
        with(binding) {
            continueButton.setOnClickListener {
                if (isAnswerSelected()) {
                    findNavController().navigate(getActionForNextFragment())
                } else {
                    showBottomSheet(message = R.string.error_empty_form)
                }
            }
        }
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
        val adapter = SingleChoiceAdapter(
            requireContext(), questions,
            object : SingleChoiceAdapter.OnItemClickListener {
                override fun onItemClick(question: Question, position: Int) {
                    question.selectedAnswerPosition = position
                    Toast.makeText(requireContext(),
                        "Você clicou no $position - ${question.selectedAnswerPosition}",
                        Toast.LENGTH_LONG).show()
                }
            })
        recyclerView.adapter = adapter
    }

    override fun getProgressBarIndex(): Int {
        return 1
    }

    override fun getProgressBarMessage(): String {
        return "1 de 6"
    }

    override fun getActionForNextFragment(): NavDirections {
        return FirstFragmentDirections.actionFirstFragmentToSecondFragment()
    }

    override fun getActionForPreviousFragment(): NavDirections? {
        return null
    }

    override fun isAnswerSelected(): Boolean {
        with(binding) {
            val adapter = rv.adapter as? SingleChoiceAdapter
            val questions = adapter?.getQuestions()
            val unansweredQuestion = questions?.get(0)
            return unansweredQuestion?.selectedAnswerPosition != null
        }
    }
}