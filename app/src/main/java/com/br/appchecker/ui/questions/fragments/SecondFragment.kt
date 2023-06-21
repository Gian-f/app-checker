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
import com.br.appchecker.databinding.FragmentSecondBinding
import com.br.appchecker.ui.questions.adapters.SingleChoiceAdapter
import com.br.appchecker.util.showBottomSheet

class SecondFragment : QuestionBaseFragment<FragmentSecondBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) ->
    FragmentSecondBinding get() = FragmentSecondBinding::inflate

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

    private fun configRecyclerView() {
        val adapter = SingleChoiceAdapter(requireContext(),
            object : SingleChoiceAdapter.OnItemClickListener {
            override fun onItemClick(question: Question, position: Int) {
                Toast.makeText(requireContext(),
                    "você clicou no $position",
                    Toast.LENGTH_SHORT)
                    .show()
            }
        })
        viewModel.questions.observe(viewLifecycleOwner) { questions ->
            adapter.submitList(questions)
            adapter.notifyDataSetChanged()
        }

        viewModel.getAllQuestions()
        binding.rvSecond.adapter = adapter
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

//    private fun getMockedQuestions(): MutableList<Question> {
//        return mutableListOf(
//            Question(
//                id = ULID.randomULID(),
//                title = "Sua receita bruta na atividade rural foi superior a R$ 142.798,50?",
//                description = "Selecione a opção que te melhor descreve",
//                answers = listOf(
//                    "Sim, acima do limite estabelecido",
//                    "Não, não recebi acima do limite estabelecido",
//                    "Não sei / Não tenho certeza",
//                    "Não se aplica a mim"
//                ),
//                selectedAnswerPosition = null
//            )
//        )
//    }
    override fun getProgressBarIndex(): Int = 2
    override fun getProgressBarMessage(): String = "2 de 6"
    override fun getActionForNextFragment() =
        SecondFragmentDirections.actionSecondFragmentToThirdFragment()
    override fun getActionForPreviousFragment() =
        SecondFragmentDirections.actionSecondFragmentToFirstFragment()
    override fun isAnswerSelected(): Boolean {
        val adapter = binding.rvSecond.adapter as? SingleChoiceAdapter
        val questions = adapter?.currentList
        val unansweredQuestion = questions?.getOrNull(0)
        return unansweredQuestion?.selectedAnswerPosition != null
    }
}