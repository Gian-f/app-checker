package com.br.appchecker.presentation.questions.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.br.appchecker.R
import com.br.appchecker.data.local.AppDatabase
import com.br.appchecker.databinding.FragmentQuestionBinding
import com.br.appchecker.domain.model.Question
import com.br.appchecker.presentation.questions.GlobalData
import com.br.appchecker.presentation.questions.adapters.SingleChoiceAdapter
import com.br.appchecker.presentation.questions.interfaces.ProgressBarListener
import com.br.appchecker.presentation.questions.viewmodels.QuestionViewModel
import com.br.appchecker.presentation.questions.viewmodels.factory.QuestionViewModelFactory
import com.br.appchecker.util.LoadingUtils
import com.br.appchecker.util.LoadingUtils.showBottomSheet
import com.google.android.material.bottomsheet.BottomSheetDialog

class QuestionFragment : Fragment() {

    private val binding by lazy {
        FragmentQuestionBinding.inflate(layoutInflater)
    }
    private val position by lazy {
        QuestionFragmentArgs.fromBundle(requireArguments()).position
    }
    private val list by lazy {
        QuestionFragmentArgs.fromBundle(requireArguments()).list
    }

    private lateinit var viewModel:QuestionViewModel

    private var bottomSheetDialog: BottomSheetDialog? = null

    private var progressBarListener: ProgressBarListener? = null

    private val adapter: SingleChoiceAdapter by lazy {
        SingleChoiceAdapter(requireContext(), object : SingleChoiceAdapter.OnItemClickListener {
            override fun onItemClick(question: Question, position: Int) {
                question.selectedAnswerPosition = position

            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupRecyclerView()
        setupListeners()
        setupButtonsVisibility()
        isFormValid()
    }

    private fun setupListeners() {
        binding.continueButton.setOnClickListener {
            if (isAnswerSelected()) {
                navigateToNextQuestion()
            } else {
                showBottomSheet(requireContext(), message = R.string.error_empty_form)
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

    private fun setupViewModel() {
        val questionDao = AppDatabase.getInstance(requireContext()).questionDao()
        val userDao = AppDatabase.getInstance(requireContext()).userDao()
        viewModel = ViewModelProvider(
            this,
            QuestionViewModelFactory(
                questionDao, userDao, requireContext()
            )
        )[QuestionViewModel::class.java]
    }

    private fun updateProgressBar(position: Int, sizeList: Int) {
        progressBarListener?.onUpdateProgressBar(position + 1, sizeList)
    }

    private fun setupObservers() {
        viewModel.questions.observe(viewLifecycleOwner) { questions ->
            GlobalData.globalQuestions.value = questions
            updateProgressBar(position, questions.size)
            adapter.submitList(findQuestionListOne(questions))
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                showLoading()
            } else {
                hideLoading()
            }
        }

        if (list.isNullOrEmpty() || GlobalData.globalQuestions.value.isNullOrEmpty()) {
            viewModel.getAllQuestionsFromFirebase()
        } else {
            val listAux = list!!.toMutableList()
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
            }
        }
    }

    private fun findQuestionListOne(questions: List<Question>): List<Question> {
        return listOf(questions[position])
    }

    private fun navigateToNextQuestion() {
        findNavController().navigate(getActionForNextFragment())
    }

    private fun getActionForNextFragment(): NavDirections {
        return if (position + 1 < (list?.size ?: GlobalData.globalQuestions.value?.size ?: 0)) {
            QuestionFragmentDirections.actionFirstFragmentSelf(
                position = position + 1,
                list = list ?: GlobalData.globalQuestions.value?.toTypedArray()
            )
        } else {
            QuestionFragmentDirections.actionFirstFragmentToResultFragment()
        }
    }

    private fun isAnswerSelected(): Boolean {
        val question = adapter.currentList.getOrNull(0)
        return question?.selectedAnswerPosition != -1
    }

    private fun isFormValid(): Boolean {
        val currentQuestion = adapter.currentList.any { question ->
            question.description.contains("Viagem")
        }
        if (currentQuestion) {
            binding.continueButton.text = getString(R.string.finalizar)
        }
        return currentQuestion
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        progressBarListener = context as? ProgressBarListener
    }

    override fun onDetach() {
        super.onDetach()
        progressBarListener = null
    }

    override fun onDestroy() {
        super.onDestroy()
        hideLoading()
        bottomSheetDialog?.dismiss()
    }
}
