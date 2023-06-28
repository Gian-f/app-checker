package com.br.appchecker.util

import android.util.Log
import android.util.Patterns
import android.widget.EditText

object ValidationUtils {
    fun isEmailValid(email: String): Boolean {
        val emailRegex = Patterns.EMAIL_ADDRESS.toRegex()
        return email.isNotBlank() && (emailRegex.matches(email))
    }

    fun isNumberValid(phone: String): Boolean {
        val numberRegex = Patterns.PHONE.toRegex()
        return phone.isNotBlank() && (numberRegex.matches(phone))
    }

    fun isCodeValid(code: List<EditText>): Boolean {
        return code.isNotEmpty()
    }

    fun isPasswordValid(password: String): Boolean {
        val minLength = 5

        // Verificar comprimento mínimo
        if (password.length < minLength) {
            return false
        }

        // Verificar se contém pelo menos uma letra minúscula
        val lowercaseRegex = Regex("[a-z]")
        if (!password.contains(lowercaseRegex)) {
            return false
        }

        // Verificar se contém pelo menos uma letra maiúscula
        val uppercaseRegex = Regex("[A-Z]")
        if (!password.contains(uppercaseRegex)) {
            return false
        }
        return true
    }

    fun isNameValid(name: String): Boolean {
        return name.isNotEmpty()
    }
}