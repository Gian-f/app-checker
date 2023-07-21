package com.br.appchecker.presentation.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.br.appchecker.databinding.FragmentResultBinding
import com.br.appchecker.presentation.result.viewmodels.ResultViewModel
import com.br.appchecker.presentation.result.viewmodels.factory.ResultViewModelFactory

class ResultFragment: Fragment() {

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
//        setupObservers()
        // Iniciar a conversa com uma mensagem de saudação
//        resultViewModel.sendMessage("Olá preciso que você me retorne se eu preciso declarar um imposto de renda ou não baseado neste JSON?")
    }

    private fun setupViewModel() {
        val viewModelFactory = ResultViewModelFactory(requireContext())
        resultViewModel = ViewModelProvider(this, viewModelFactory)[ResultViewModel::class.java]
    }

//    private fun setupObservers() {
//        // Observar as mudanças na lista de mensagens no ViewModel
//        resultViewModel.chatMessages.observe(viewLifecycleOwner) { messages ->
//            val message = messages.lastOrNull()
//            message?.let { text ->
//                binding.tvResult.text = text
//            }
//        }
//    }
}