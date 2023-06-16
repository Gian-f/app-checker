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
import com.br.appchecker.databinding.FragmentFirstBinding
import com.br.appchecker.ui.questions.fragments.FirstFragmentDirections
import com.br.appchecker.ui.questions.QuestionBaseFragment
import com.br.appchecker.ui.questions.adapters.SingleChoiceAdapter
import com.br.appchecker.ui.questions.viewmodels.QuestionViewModel
import com.br.appchecker.ui.questions.viewmodels.factory.QuestionViewModelFactory
import com.br.appchecker.util.showBottomSheet
import ulid.ULID

class FirstFragment : QuestionBaseFragment<FragmentFirstBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) ->
    FragmentFirstBinding = FragmentFirstBinding::inflate

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
        setupRecyclerView()
        setupListeners()
    }

    private fun setupViewModel() {
        val questionApiClient = QuestionService()
        val questionRepository = QuestionRepositoryImpl(questionApiClient)
        viewModel = ViewModelProvider(
            this,
            QuestionViewModelFactory(questionRepository))[QuestionViewModel::class.java]
    }

    private fun setupListeners() {
        binding.continueButton.setOnClickListener {
            if (isAnswerSelected()) {
                findNavController().navigate(getActionForNextFragment())
            } else {
                showBottomSheet(message = R.string.error_empty_form)
            }
        }
    }

    private fun setupRecyclerView() {
        val adapter = SingleChoiceAdapter(requireContext(), getMockedQuestions(),
            object : SingleChoiceAdapter.OnItemClickListener {
                override fun onItemClick(question: Question, position: Int) {
                    question.selectedAnswerPosition = position
                    Toast.makeText(
                        requireContext(),
                        "Você clicou em $position - ${question.selectedAnswerPosition}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })

//        viewModel.questions.observe(viewLifecycleOwner) { questions ->
//            adapter.submitList(questions)
//        }
//        viewModel.getAllQuestions()
        binding.rv.adapter = adapter
    }

    private fun getMockedQuestions(): MutableList<Question> {
        return mutableListOf(
            Question(
                id = ULID.randomULID(),
                title = "Seus rendimentos tributáveis foram superiores a R$ 28.559,70 no ano passado?",
                description = "Selecione a opção que melhor te descreve",
                answers = listOf(
                    "Sim, acima do limite estabelecido",
                    "Não, não recebi acima do limite estabelecido",
                    "Não sei / Não tenho certeza",
                    "Não se aplica a mim"
                ),
                selectedAnswerPosition = null
            )
        )
    }

    override fun getProgressBarIndex(): Int = 1

    override fun getProgressBarMessage(): String = "1 de 6"

    override fun getActionForNextFragment() =
        FirstFragmentDirections.actionFirstFragmentToSecondFragment()

    override fun getActionForPreviousFragment(): Nothing? = null

    override fun isAnswerSelected(): Boolean {
        val adapter = binding.rv.adapter as? SingleChoiceAdapter
        val questions = adapter?.getQuestions()
        val unansweredQuestion = questions?.get(0)
        return unansweredQuestion?.selectedAnswerPosition != null
    }
}
