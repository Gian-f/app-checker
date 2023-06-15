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
import com.br.appchecker.databinding.FragmentSixthBinding
import com.br.appchecker.ui.questions.adapters.SingleChoiceAdapter
import com.br.appchecker.util.showBottomSheet
import ulid.ULID

class SixthFragment : BaseFragment<FragmentSixthBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) ->
    FragmentSixthBinding get() = FragmentSixthBinding::inflate

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
//                    findNavController().navigate(nextAction)
                    showBottomSheet(message = R.string.error_not_implemented_yet)
                } else {
                    showBottomSheet(message = R.string.error_empty_form)
                }
            }

            backButton.setOnClickListener {
                findNavController().navigate(getActionForPreviousFragment())
            }
        }
    }

    private fun configRecyclerView() {
        binding.rvSixth.layoutManager = LinearLayoutManager(requireContext())
        val questions = mutableListOf<Question>()
        questions.add(
            Question(
                id = ULID.randomULID(),
                title = "Você se tornou residente no Brasil em algum mês e estava nessa condição em 31 de dezembro do ano-calendário?",
                description = "Selecione a opção que melhor te descreve",
                answers = listOf(
                    "Sim,me tornei residente e estava nessa condição",
                    "Não, não me tornei residente no brasil no ano-calendário",
                    "Não sei / Não tenho certeza",
                    "Não se aplica a mim"
                ),
                selectedAnswerPosition = null))

        val adapter = SingleChoiceAdapter(requireContext(),questions, object :
            SingleChoiceAdapter.OnItemClickListener {
            override fun onItemClick(question: Question, position: Int) {
                Toast.makeText(requireContext(), "Você clicou na posição $position", Toast.LENGTH_SHORT).show()
            }
        })
        binding.rvSixth.adapter = adapter
    }

    override fun getProgressBarIndex(): Int {
        return 6
    }

    override fun getProgressBarMessage(): String {
        return "6 de 6"
    }

    override fun getActionForNextFragment(): NavDirections {
        return TODO()
    }

    override fun getActionForPreviousFragment(): NavDirections {
        return SixthFragmentDirections.actionSixthFragmentToFifthFragment()
    }

    override fun isAnswerSelected(): Boolean {
        with(binding) {
            val adapter = rvSixth.adapter as? SingleChoiceAdapter
            val questions = adapter?.getQuestions()
            val unansweredQuestion = questions?.get(0)
            return unansweredQuestion?.selectedAnswerPosition != null
        }
    }
}
