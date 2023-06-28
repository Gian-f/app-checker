package com.br.appchecker.util

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.br.appchecker.R
import com.br.appchecker.databinding.BottomSheetBinding
import com.br.appchecker.databinding.LoadingBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}

fun Activity.showNotification(title: String, description: String) {
    val notificationHelper = LocalNotificationManager(this)
    notificationHelper.showNotification(title, description, intent)
}

fun Activity.showToast(message: String) {
    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
}