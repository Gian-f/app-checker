package com.br.appchecker.ui.questions.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.br.appchecker.R
import com.br.appchecker.databinding.FragmentFirstBinding
import com.br.appchecker.domain.model.Question
import com.br.appchecker.ui.questions.adapters.SingleChoiceAdapter
import com.br.appchecker.util.showBottomSheet

class FirstFragment : QuestionBaseFragment<FragmentFirstBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) ->
    FragmentFirstBinding = FragmentFirstBinding::inflate

    private val position by lazy {
        FirstFragmentArgs.fromBundle(requireArguments()).position
    }

    private val list by lazy {
        FirstFragmentArgs.fromBundle(requireArguments()).list
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
        setupViewModel()
        setupRecyclerView()
        setupListeners()
        setupButtonsVisibility()
    }

    private fun setupListeners() {
        binding.continueButton.setOnClickListener {
            if (isAnswerSelected()) {
                navigateToNextQuestion()
            } else {
                showBottomSheet(message = R.string.error_empty_form)
            }
        }
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupRecyclerView() {
        val adapter = SingleChoiceAdapter(requireContext(),
            object : SingleChoiceAdapter.OnItemClickListener {
                override fun onItemClick(question: Question, position: Int) {
                    question.selectedAnswerPosition = position
                    Toast.makeText(
                        requireContext(),
                        "Você clicou em $position - ${question.selectedAnswerPosition}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })

        viewModel.questions.observe(viewLifecycleOwner) { questions ->
            GlobalData.questions.value = questions
            updateProgressBar(position, questions.size)
            adapter.submitList(findQuestionListOne(questions))
            adapter.notifyDataSetChanged()
        }

        if ((list.isNullOrEmpty()) || GlobalData.questions.value.isNullOrEmpty()) {
            viewModel.getAllQuestions()
        } else {
            val listAux = ArrayList<Question>()
            listAux.addAll((list ?: arrayOf()))
            GlobalData.questions.value = listAux
            updateProgressBar(position, listAux.size)
            adapter.submitList(findQuestionListOne(listAux))
            adapter.notifyDataSetChanged()
        }
        binding.rv.adapter = adapter
    }

    private fun setupButtonsVisibility() {
        if (position > 0) {
            with(binding) {
                backButton.visibility = View.VISIBLE
                continueButton.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            }
        }
    }

    private fun findQuestionListOne(questions: List<Question>): MutableList<Question> {
        return mutableListOf(questions[position])
    }

    private fun navigateToNextQuestion() {
        findNavController().navigate(getActionForNextFragment())
    }

    override fun getProgressBarIndex(): Int = position + 1

    override fun getProgressBarMessage(): String =
        "${position + 1} de ${list?.size ?: GlobalData.questions.value?.size ?: 1}"

    override fun getActionForNextFragment(): NavDirections {
        return if (position + 1 < (list?.size ?: GlobalData.questions.value?.size ?: 0)) {
            FirstFragmentDirections.actionFirstFragmentSelf(
                position = position + 1,
                list = list ?: GlobalData.questions.value?.toTypedArray() ?: arrayOf()
            )
        } else FirstFragmentDirections.actionFirstFragmentToResultFragment()
    }

    override fun getActionForPreviousFragment(): Nothing? = null

    override fun isAnswerSelected(): Boolean {
        val adapter = binding.rv.adapter as? SingleChoiceAdapter
        val questions = adapter?.currentList
        val unansweredQuestion = questions?.getOrNull(0)
        return unansweredQuestion?.selectedAnswerPosition != null
    }
}

