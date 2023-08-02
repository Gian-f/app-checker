package com.br.appchecker.presentation.result

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
            val message = "De acordo com as informações fornecidas, por favor, informe se é necessário declarar o imposto de renda sem entrar em muitos detalhes, sendo breve, por favor."
            resultViewModel.sendMessage(message, questions)
        }
        resultViewModel.chatMessages.observe(viewLifecycleOwner) { response ->
            showTextLetterByLetter(response)
        }
    }
    private fun showTextLetterByLetter(text: String) {
        val textView = binding.tvResult
        var currentCharIndex = 0

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (currentCharIndex < text.length) {
                    val currentText = text.substring(0, currentCharIndex + 1)
                    textView.text = currentText
                    currentCharIndex++
                    handler.postDelayed(this, 50)
                }
            }
        }, 50)
    }
}