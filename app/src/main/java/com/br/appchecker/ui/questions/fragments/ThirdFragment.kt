package com.br.appchecker.ui.questions.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.br.appchecker.R
import com.br.appchecker.domain.model.Question
import com.br.appchecker.data.remote.request.QuestionRequest
import com.br.appchecker.databinding.FragmentThirdBinding
import com.br.appchecker.ui.questions.adapters.SingleChoiceAdapter
import com.br.appchecker.util.showBottomSheet

class ThirdFragment : QuestionBaseFragment<FragmentThirdBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) ->
    FragmentThirdBinding
        get() = FragmentThirdBinding::inflate

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
                    findNavController().navigate(getActionForNextFragment())
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
                Toast.makeText(requireContext(),
                    "você clicou no $position",
                    Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.questions.observe(viewLifecycleOwner) { questions ->
            adapter.submitList(questions)
            adapter.notifyItemChanged(
                questions[0].selectedAnswerPosition!!)
        }
        viewModel.getAllQuestions(QuestionRequest(1))
        binding.rvThird.adapter = adapter
    }

//    private fun getMockedQuestions() : MutableList<Question> {
//        return mutableListOf(
//            Question(
//                id = ULID.randomULID(),
//                title = "Você obteve ganho de capital na venda de bens ou direitos sujeito ao imposto?",
//                description = "Selecione a opção que te melhor descreve",
//                answers = listOf(
//                    "Sim, obtive ganho de capital em venda de bens",
//                    "Não, não obtive ganho de capital em venda de bens",
//                    "Não sei / Não tenho certeza",
//                    "Não se aplica a mim"),
//                selectedAnswerPosition = null)
//        )
//    }

    override fun getProgressBarIndex() = 3

    override fun getProgressBarMessage() = "3 de 6"

    override fun getActionForNextFragment() =
        ThirdFragmentDirections.actionThirdFragmentToFourthFragment()


    override fun getActionForPreviousFragment() =
        ThirdFragmentDirections.actionThirdFragmentToSecondFragment()


    override fun isAnswerSelected(): Boolean {
        val adapter = binding.rvThird.adapter as? SingleChoiceAdapter
        val questions = adapter?.currentList
        val unansweredQuestion = questions?.getOrNull(0)
        return unansweredQuestion?.selectedAnswerPosition != null
    }
}