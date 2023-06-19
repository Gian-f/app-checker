package com.br.appchecker.ui.questions.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.br.appchecker.R
import com.br.appchecker.data.model.Question
import com.br.appchecker.databinding.FragmentSixthBinding
import com.br.appchecker.ui.questions.ResultActivity
import com.br.appchecker.ui.questions.adapters.SingleChoiceAdapter
import com.br.appchecker.util.showBottomSheet
import ulid.ULID

class SixthFragment : QuestionBaseFragment<FragmentSixthBinding>() {

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
        setupViewModel()
        configRecyclerView()
        setupListeners()
    }

    private fun setupListeners() {
        with(binding) {
            continueButton.setOnClickListener {
                if (isAnswerSelected()) {
                    val intent = Intent(context, ResultActivity::class.java)
                    context?.startActivity(intent)
//                    showBottomSheet(message = R.string.error_not_implemented_yet)
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
        val adapter = SingleChoiceAdapter(requireContext(),
            object : SingleChoiceAdapter.OnItemClickListener {
            override fun onItemClick(question: Question, position: Int) {
                Toast.makeText(requireContext(), "Você clicou na posição $position", Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.questions.observe(viewLifecycleOwner) { questions ->
            adapter.submitList(questions)
            adapter.notifyDataSetChanged()
            println(questions[0].selectedAnswerPosition)
        }
        viewModel.getAllQuestions()
        binding.rvSixth.adapter = adapter
    }

//    private fun getMockedQuestions(): MutableList<Question> {
//        return mutableListOf(
//            Question(
//                id = ULID.randomULID(),
//                title = "Você se tornou residente no Brasil em algum mês e estava nessa condição em 31 de dezembro do ano-calendário?",
//                description = "Selecione a opção que melhor te descreve",
//                answers = listOf(
//                    "Sim,me tornei residente e estava nessa condição",
//                    "Não, não me tornei residente no brasil no ano-calendário",
//                    "Não sei / Não tenho certeza",
//                    "Não se aplica a mim"
//                    ),
//                selectedAnswerPosition = null
//            )
//        )
//    }

    override fun getProgressBarIndex() = 6

    override fun getProgressBarMessage() = "6 de 6"

    override fun getActionForNextFragment() = TODO()

    override fun getActionForPreviousFragment() =
        SixthFragmentDirections.actionSixthFragmentToFifthFragment()

    override fun isAnswerSelected(): Boolean {
        val adapter = binding.rvSixth.adapter as? SingleChoiceAdapter
        val questions = adapter?.currentList
        val unansweredQuestion = questions?.getOrNull(0)
        return unansweredQuestion?.selectedAnswerPosition != null
    }
}