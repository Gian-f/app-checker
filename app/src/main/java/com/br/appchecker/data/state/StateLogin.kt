package com.br.appchecker.data.state

import com.br.appchecker.domain.model.User

sealed class StateLogin<out T> {

    data class Success<out T>(
        val data: User,
        val info: String
    ) : StateLogin<T>()

    data class Error(
        val message: String = "Certifique-se que o dispositivo está conectado à internet e se as configurações estão corretas.",
        val code: Int = 0,
        val txt: String = "\n\nDeseja tentar novamente?",
        val title: String = "Ocorreu um problema.",
        val exception: Exception? = null
    ) : StateLogin<Nothing>()
}
