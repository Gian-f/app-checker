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

    private val _chatMessages = MutableLiveData<String>()
    val chatMessages: LiveData<String> get() = _chatMessages

    fun sendMessage(message: String, questions: List<Question>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.sendMessage(message, questions)
                if (response is StateInfo.Success) {
                    val currentMessages = _chatMessages.value ?: ""
                    val newMessages = "$currentMessages\n${response.data}"
                    _chatMessages.postValue(newMessages)
                }
            } catch (e: Exception) {
                // Trate o erro, se necessário
            }
        }
    }
}