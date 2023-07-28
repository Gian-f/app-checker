package com.br.appchecker.presentation.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.br.appchecker.databinding.FragmentResultBinding
import com.br.appchecker.presentation.questions.GlobalData
import com.br.appchecker.presentation.result.viewmodels.ResultViewModel
import com.br.appchecker.presentation.result.viewmodels.factory.ResultViewModelFactory

class ResultFragment : Fragment() {

    private val binding by lazy { FragmentResultBinding.inflate(layoutInflater) }

    private lateinit var resultViewModel: ResultViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupObservers()
    }

    private fun setupViewModel() {
        val viewModelFactory = ResultViewModelFactory(requireContext())
        resultViewModel = ViewModelProvider(this, viewModelFactory)[ResultViewModel::class.java]
    }

    private fun setupObservers() {
        GlobalData.globalQuestions.observe(viewLifecycleOwner) { questions ->
            // Aqui você consegue colocar a mensagem que achar melhor
            val message = "De acordo com este JSON, defina se preciso declarar imposto de renda ou não"
            resultViewModel.sendMessage(message, questions)
        }
        resultViewModel.chatMessages.observe(viewLifecycleOwner) { response ->
            binding.tvResult.text = response
        }
    }
}