package com.br.appchecker.presentation.result.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.appchecker.data.repository.ai.ResultRepositoryImpl
import com.br.appchecker.data.state.StateInfo
import com.br.appchecker.domain.model.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResultViewModel(
    private val repository: ResultRepositoryImpl
) : ViewModel() {

    suspend fun sendMessage(message: String, questions: List<Question>): String {
        return try {
            when (val response = repository.sendMessage(message, questions)) {
                is StateInfo.Success -> response.data.toString()
                is StateInfo.Error -> "Error: ${response.message}"
            }
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}