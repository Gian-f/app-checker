package com.br.appchecker.presentation.questions.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.viewbinding.ViewBinding
import com.br.appchecker.data.local.AppDatabase
import com.br.appchecker.presentation.questions.interfaces.ProgressBarListener
import com.br.appchecker.presentation.questions.viewmodels.QuestionViewModel
import com.br.appchecker.presentation.questions.viewmodels.factory.QuestionViewModelFactory

abstract class QuestionBaseFragment<T : ViewBinding> : Fragment() {

    protected abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> T

    protected lateinit var binding: T

    lateinit var viewModel: QuestionViewModel

    private var progressBarListener: ProgressBarListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = bindingInflater.invoke(inflater, container, false)
        return binding.root
    }

    fun updateProgressBar(position: Int, sizeList: Int) {
        progressBarListener?.onUpdateProgressBar(position + 1, sizeList)
    }

    protected fun setupViewModel() {
        val questionDao = AppDatabase.getInstance(requireContext()).questionDao()
        val userDao = AppDatabase.getInstance(requireContext()).userDao()
        viewModel = ViewModelProvider(
            this,
            QuestionViewModelFactory(
                questionDao, userDao
            )
        )[QuestionViewModel::class.java]
    }

    abstract fun getProgressBarIndex(): Int

    abstract fun getProgressBarMessage(): String

    abstract fun getActionForNextFragment(): NavDirections

    abstract fun getActionForPreviousFragment(): NavDirections?

    abstract fun isAnswerSelected(): Boolean

    override fun onAttach(context: Context) {
        super.onAttach(context)
        progressBarListener = context as? ProgressBarListener
    }

    override fun onDetach() {
        super.onDetach()
        progressBarListener = null
    }
}