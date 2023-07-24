package com.br.appchecker.presentation.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.br.appchecker.databinding.FragmentResultBinding
import com.br.appchecker.domain.model.Question
import com.br.appchecker.presentation.questions.GlobalData
import com.br.appchecker.presentation.result.viewmodels.ResultViewModel
import com.br.appchecker.presentation.result.viewmodels.factory.ResultViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        setupObservers()
    }

    private fun setupViewModel() {
        val viewModelFactory = ResultViewModelFactory(requireContext())
        resultViewModel = ViewModelProvider(this, viewModelFactory)[ResultViewModel::class.java]
    }

    private fun setupObservers() {

        GlobalData.globalQuestions.observe(viewLifecycleOwner) { questions ->
            lifecycleScope.launch(Dispatchers.IO) {
                sendMessageWithQuestions(questions)
            }
        }
    }

    private suspend fun sendMessageWithQuestions(questions: List<Question>) {
        // Aqui você pode formatar a mensagem da forma que desejar, ou usar a lista de perguntas diretamente, como preferir.
        val message = "De acordo com este JSON, defina se preciso declarar imposto de renda ou não"

        val response = resultViewModel.sendMessage(message, questions)
        withContext(Dispatchers.Main) {
            binding.tvResult.text = response
        }
    }
}