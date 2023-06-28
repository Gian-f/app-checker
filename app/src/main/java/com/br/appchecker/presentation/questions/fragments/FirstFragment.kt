package com.br.appchecker.presentation.questions.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.br.appchecker.R
import com.br.appchecker.databinding.FragmentFirstBinding
import com.br.appchecker.domain.model.Question
import com.br.appchecker.presentation.questions.GlobalData
import com.br.appchecker.presentation.questions.adapters.SingleChoiceAdapter
import com.br.appchecker.util.LoadingUtils.showBottomSheet

class FirstFragment : QuestionBaseFragment<FragmentFirstBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFirstBinding =
        FragmentFirstBinding::inflate

    private val position by lazy {
        FirstFragmentArgs.fromBundle(requireArguments()).position
    }

    private val list by lazy {
        FirstFragmentArgs.fromBundle(requireArguments()).list
    }

    private val adapter: SingleChoiceAdapter by lazy {
        SingleChoiceAdapter(requireContext(), object : SingleChoiceAdapter.OnItemClickListener {
            override fun onItemClick(question: Question, position: Int) {
                question.selectedAnswerPosition = position
                Toast.makeText(
                    requireContext(),
                    "Você clicou em $position - ${question.selectedAnswerPosition}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
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
                showBottomSheet(requireContext(),message = R.string.error_empty_form)
            }
        }
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupRecyclerView() {
        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL,
            )
        )
        viewModel.questions.observe(viewLifecycleOwner) { questions ->
            GlobalData.globalQuestions.value = questions
            updateProgressBar(position, questions.size)
            adapter.submitList(findQuestionListOne(questions))
        }
        if (list.isNullOrEmpty() || GlobalData.globalQuestions.value.isNullOrEmpty()) {
            viewModel.getAllQuestions()
        } else {
            val listAux = ArrayList<Question>()
            listAux.addAll(list!!)
            GlobalData.globalQuestions.value = listAux
            updateProgressBar(position, listAux.size)
            adapter.submitList(findQuestionListOne(listAux))
        }
    }

    private fun setupButtonsVisibility() {
        if (position > 0) {
            with(binding) {
                backButton.visibility = View.VISIBLE
                continueButton.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            }
        }
    }

    private fun findQuestionListOne(questions: List<Question>): List<Question> {
        return listOf(questions[position])
    }

    private fun navigateToNextQuestion() {
        findNavController().navigate(getActionForNextFragment())
    }

    override fun getProgressBarIndex(): Int = position + 1

    override fun getProgressBarMessage(): String =
        "${position + 1} de ${list?.size ?: GlobalData.globalQuestions.value?.size ?: 1}"

    override fun getActionForNextFragment(): NavDirections {
        return if (position + 1 < (list?.size ?: GlobalData.globalQuestions.value?.size ?: 0)) {
            FirstFragmentDirections.actionFirstFragmentSelf(
                position = position + 1,
                list = list ?: GlobalData.globalQuestions.value?.toTypedArray()
            )
        } else {
            FirstFragmentDirections.actionFirstFragmentToResultFragment()
        }
    }

    override fun getActionForPreviousFragment(): Nothing? = null

    override fun isAnswerSelected(): Boolean {
        val questions = adapter.currentList
        val unansweredQuestion = questions.getOrNull(0)
        return unansweredQuestion?.selectedAnswerPosition != null
    }
}
