package com.br.appchecker.ui.questions

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.br.appchecker.R
import com.br.appchecker.data.model.Question
import com.br.appchecker.databinding.FragmentFirstBinding
import com.br.appchecker.ui.questions.adapters.SingleChoiceQuestionAdapter
import com.br.appchecker.ui.questions.interfaces.ProgressBarListener
import com.br.appchecker.util.showBottomSheet
import ulid.ULID

class FirstFragment : Fragment() {

    private val binding: FragmentFirstBinding by
    lazy { FragmentFirstBinding.inflate(layoutInflater) }
    private var progressBarListener: ProgressBarListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configRecyclerView()
        setupListeners()
        progressBarListener?.onUpdateProgressBar(1, "1 de 10")
    }

    private fun setupListeners() {
        binding.continueButton.setOnClickListener {
//            showBottomSheet(message = R.string.error_generic)
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    private fun configRecyclerView() {
        val recyclerView = binding.rv
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val questions = listOf<Question>().toMutableList()
        questions.add(Question(ULID.randomULID(),
            "Quanto de renda bruta você ganhou este ano?",
            "Selecione quanto de renda você ganhou este ano",
            listOf("de R$1.000,00 a 5.000,00",
                "de R$5.000,00 a R$10.000,00",
                "de R$5.000,00 a R$10.000,00",
                "de R$5.000,00 a R$10.000,00")
            , 0))
        val adapter = SingleChoiceQuestionAdapter(questions, object :
            SingleChoiceQuestionAdapter.ViewHolder.OnItemClickListener {
            override fun onItemClick(question: Question, position: Int) {
                println(questions)
                Toast.makeText(requireContext(), "teste", Toast.LENGTH_SHORT).show()
            }
        })
        recyclerView.adapter = adapter
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