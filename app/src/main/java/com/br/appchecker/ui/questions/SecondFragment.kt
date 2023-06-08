package com.br.appchecker.ui.questions

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.br.appchecker.databinding.FragmentSecondBinding
import com.br.appchecker.ui.questions.interfaces.ProgressBarListener

class SecondFragment : Fragment() {

    private val binding: FragmentSecondBinding by
    lazy { FragmentSecondBinding.inflate(layoutInflater) }

    private var progressBarListener: ProgressBarListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBarListener?.onUpdateProgressBar(2, "2 de 6")
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        progressBarListener = context as? ProgressBarListener
    }
    override fun onDetach() {
        super.onDetach()
        progressBarListener = null
    }
}