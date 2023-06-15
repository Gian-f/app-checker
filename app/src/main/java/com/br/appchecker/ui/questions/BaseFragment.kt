package com.br.appchecker.ui.questions

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.viewbinding.ViewBinding
import com.br.appchecker.ui.questions.interfaces.ProgressBarListener

abstract class BaseFragment<T : ViewBinding> : Fragment() {

    protected abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> T

    protected lateinit var binding: T

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
        progressBarListener?.onUpdateProgressBar(getProgressBarIndex(), getProgressBarMessage() )
    }

    protected abstract fun getProgressBarIndex(): Int

    protected abstract fun getProgressBarMessage(): String

    protected abstract fun getActionForNextFragment(): NavDirections

    protected abstract fun getActionForPreviousFragment(): NavDirections?

    protected abstract fun isAnswerSelected(): Boolean

    override fun onAttach(context: Context) {
        super.onAttach(context)
        progressBarListener = context as? ProgressBarListener
    }

    override fun onDetach() {
        super.onDetach()
        progressBarListener = null
    }
}
