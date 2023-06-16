package com.br.appchecker.ui.questions.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.viewbinding.ViewBinding
import com.br.appchecker.data.remote.service.QuestionService
import com.br.appchecker.data.repository.QuestionRepositoryImpl
import com.br.appchecker.ui.questions.interfaces.ProgressBarListener
import com.br.appchecker.ui.questions.viewmodels.QuestionViewModel
import com.br.appchecker.ui.questions.viewmodels.factory.QuestionViewModelFactory

abstract class QuestionBaseFragment<T : ViewBinding> : Fragment() {

    protected abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> T

    protected lateinit var binding: T

    private lateinit var viewModel: QuestionViewModel

    private var progressBarListener: ProgressBarListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = bindingInflater.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBarListener?.onUpdateProgressBar(getProgressBarIndex(), getProgressBarMessage())
    }

    protected fun setupViewModel() {
        val questionApiClient = QuestionService()
        val questionRepository = QuestionRepositoryImpl(questionApiClient)
        viewModel = ViewModelProvider(this,
            QuestionViewModelFactory(questionRepository)
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