package com.br.appchecker.presentation.result.viewmodels.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.br.appchecker.data.remote.service.ResultService
import com.br.appchecker.data.repository.ai.ResultRepositoryImpl
import com.br.appchecker.presentation.result.viewmodels.ResultViewModel

class ResultViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory  {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
            return ResultViewModel(
                repository = ResultRepositoryImpl(context)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}