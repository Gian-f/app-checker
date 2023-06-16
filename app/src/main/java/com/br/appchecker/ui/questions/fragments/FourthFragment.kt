package com.br.appchecker.ui.questions.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.br.appchecker.R
import com.br.appchecker.data.model.Question
import com.br.appchecker.data.remote.service.QuestionService
import com.br.appchecker.data.repository.QuestionRepositoryImpl
import com.br.appchecker.databinding.FragmentFourthBinding
import com.br.appchecker.ui.questions.fragments.FourthFragmentDirections
import com.br.appchecker.ui.questions.QuestionBaseFragment
import com.br.appchecker.ui.questions.adapters.SingleChoiceAdapter
import com.br.appchecker.ui.questions.viewmodels.QuestionViewModel
import com.br.appchecker.ui.questions.viewmodels.factory.QuestionViewModelFactory
import com.br.appchecker.util.showBottomSheet
import ulid.ULID

class FourthFragment : QuestionBaseFragment<FragmentFourthBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) ->
    FragmentFourthBinding get() = FragmentFourthBinding::inflate

    private lateinit var viewModel: QuestionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = bindingInflater.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        configRecyclerView()
        setupListeners()
    }

    private fun setupViewModel() {
        val questionApiClient = QuestionService()
        val questionRepository = QuestionRepositoryImpl(questionApiClient)
        viewModel = ViewModelProvider(
            this,
            QuestionViewModelFactory(questionRepository)
        )[QuestionViewModel::class.java]
    }

    private fun setupListeners() {
        with(binding) {
            continueButton.setOnClickListener {
                if (isAnswerSelected()) {
                    findNavController().navigate(getActionForNextFragment())
                } else {
                    showBottomSheet(message = R.string.error_empty_form)
                }
            }
            backButton.setOnClickListener {
                findNavController().navigate(getActionForPreviousFragment())
            }
        }
    }

    private fun configRecyclerView() {
        val adapter = SingleChoiceAdapter(requireContext(),getMockedQuestions(),
            object : SingleChoiceAdapter.OnItemClickListener {
            override fun onItemClick(question: Question, position: Int) {
                Toast.makeText(requireContext(),
                "você clicou no $position",
                Toast.LENGTH_SHORT).show()
            }
        })
        binding.rvFourth.adapter = adapter
    }

    private fun getMockedQuestions(): MutableList<Question> {
        return mutableListOf(
            Question(
                id = ULID.randomULID(),
                title = "Você tinha a posse ou propriedade, em 31 de dezembro do ano-calendário, de bens ou direitos (incluindo terra nua) acima de R$ 300.000,00?",
                description = "Selecione a opção que te melhor descreve",
                answers = listOf(
                    "Sim, possuo posse ou propriedade acima do limite estabelecido",
                    "Não, não possuo posse ou propriedade acima do limite estabelecido",
                    "Não sei / Não tenho certeza",
                    "Não se aplica a mim"),
                selectedAnswerPosition = null)
        )
    }

    override fun getProgressBarIndex() = 4

    override fun getProgressBarMessage() = "4 de 6"

    override fun getActionForNextFragment() =
        FourthFragmentDirections.actionFourthFragmentToFifthFragment()

    override fun getActionForPreviousFragment() =
        FourthFragmentDirections.actionFourthFragmentToThirdFragment()

    override fun isAnswerSelected(): Boolean {
        with(binding) {
            val adapter = rvFourth.adapter as? SingleChoiceAdapter
            val questions = adapter?.getQuestions()
            val unansweredQuestion = questions?.get(0)
            return unansweredQuestion?.selectedAnswerPosition != null
        }
    }
}