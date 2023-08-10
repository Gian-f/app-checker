package com.br.appchecker.util

import com.br.appchecker.util.enums.EnumAuthErrors
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

object FirebaseUtils {

    private fun getAuth() = FirebaseAuth.getInstance()

    fun getUserId(): String? = getAuth().currentUser?.uid

    fun isAuthenticated(): Boolean = getAuth().currentUser != null

    fun getErrorMessage(error: Exception): String {
        val errorCode = (error as? FirebaseAuthException)?.errorCode
        val firebaseAuthError = EnumAuthErrors.values().find { it.name == errorCode }
        return firebaseAuthError?.message ?: EnumAuthErrors.UNKNOWN_ERROR.message
    }
}
