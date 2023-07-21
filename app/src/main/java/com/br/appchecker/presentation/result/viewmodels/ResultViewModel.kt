package com.br.appchecker.presentation.result.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.appchecker.data.repository.ai.ResultRepositoryImpl
import com.br.appchecker.data.state.StateInfo
import kotlinx.coroutines.launch

class ResultViewModel(
    private val repository: ResultRepositoryImpl
    ) : ViewModel() {

    private val _chatMessages = MutableLiveData<List<String>>()

    val chatMessages: LiveData<List<String>> get() = _chatMessages


    fun sendMessage(message: String) {
        viewModelScope.launch {
            val response = repository.sendMessage(message)
            handleChatResponse(response)
        }
    }

    fun receiveMessage(message: String) {
        viewModelScope.launch {
            val response = repository.receiveMessage(message)
            handleChatResponse(response)
        }
    }

    private fun handleChatResponse(response: StateInfo<String>) {
        when (response) {
            is StateInfo.Success -> {
                val currentMessages = _chatMessages.value ?: emptyList()
                val newMessages = currentMessages.toMutableList()
                newMessages.add(response.data.toString())
                _chatMessages.value = newMessages
            }
            is StateInfo.Error -> {
                // Trate o erro, se necessário
            }
        }
    }
}