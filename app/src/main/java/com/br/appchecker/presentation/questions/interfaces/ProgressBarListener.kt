package com.br.appchecker.presentation.questions.interfaces

interface ProgressBarListener {
    fun onUpdateProgressBar(progress: Int, max: Int)
}