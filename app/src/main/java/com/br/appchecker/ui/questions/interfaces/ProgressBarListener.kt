package com.br.appchecker.ui.questions.interfaces

interface ProgressBarListener {
    fun onUpdateProgressBar(progress: Int, step: String)
}