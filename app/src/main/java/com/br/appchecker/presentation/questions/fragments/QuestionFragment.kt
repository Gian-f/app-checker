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
import com.br.appchecker.databinding.FragmentQuestionBinding
import com.br.appchecker.domain.model.Question
import com.br.appchecker.presentation.questions.GlobalData
import com.br.appchecker.presentation.questions.adapters.SingleChoiceAdapter
import com.br.appchecker.util.LoadingUtils
import com.br.appchecker.util.LoadingUtils.showBottomSheet
import com.google.android.material.bottomsheet.BottomSheetDialog

class QuestionFragment : QuestionBaseFragment<FragmentQuestionBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentQuestionBinding =
        FragmentQuestionBinding::inflate

    private val position by lazy {
        QuestionFragmentArgs.fromBundle(requireArguments()).position
    }

    private val list by lazy {
        QuestionFragmentArgs.fromBundle(requireArguments()).list
    }

    private var bottomSheetDialog: BottomSheetDialog? = null

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
        with(binding) {
            rv.adapter = adapter
            rv.addItemDecoration(
                DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
            )
        }
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.questions.observe(viewLifecycleOwner) { questions ->
            GlobalData.globalQuestions.value = questions
            updateProgressBar(position, questions.size)
            adapter.submitList(findQuestionListOne(questions))
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.continueButton.visibility = View.GONE
                showLoading()
            } else {
                hideLoading()
            }
        }

        if (list.isNullOrEmpty() || GlobalData.globalQuestions.value.isNullOrEmpty()) {
            viewModel.getAllQuestionsFromFirebase()
        } else {
            val listAux = ArrayList<Question>()
            listAux.addAll(list!!)
            GlobalData.globalQuestions.value = listAux
            updateProgressBar(position, listAux.size)
            adapter.submitList(findQuestionListOne(listAux))
        }
    }
    private fun showLoading() {
        binding.continueButton.visibility = View.GONE
        bottomSheetDialog = LoadingUtils.showLoadingSheet(requireContext())
    }

    private fun hideLoading() {
        binding.continueButton.visibility = View.VISIBLE
        bottomSheetDialog?.let { LoadingUtils.dismissLoadingSheet(it) }
        bottomSheetDialog = null
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
            QuestionFragmentDirections.actionFirstFragmentSelf(
                position = position + 1,
                list = list ?: GlobalData.globalQuestions.value?.toTypedArray()
            )
        } else {
            QuestionFragmentDirections.actionFirstFragmentToResultFragment()
        }
    }

    override fun getActionForPreviousFragment(): Nothing? = null

    override fun isAnswerSelected(): Boolean {
        val questions = adapter.currentList
        val unansweredQuestion = questions.getOrNull(0)
        return unansweredQuestion?.selectedAnswerPosition != null
    }

    override fun onDestroy() {
        super.onDestroy()
        hideLoading()
        bottomSheetDialog?.dismiss()
    }
}
