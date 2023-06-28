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

fun Fragment.showBottomSheet(
    titleDialog: Int? = null,
    titleButton: Int? = null,
    message: Int,
    onClick: () -> Unit = {}
) {
    val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
    val bottomSheetBinding: BottomSheetBinding =
        BottomSheetBinding.inflate(layoutInflater, null, false)

    bottomSheetBinding.apply {
        textTitle.text = getString(titleDialog ?: R.string.text_title_bottom_sheet)
        textMessage.text = getText(message)
        btnClick.text = getString(titleButton ?: R.string.text_button_bottom_sheet)
        btnClick.setOnClickListener {
            onClick()
            bottomSheetDialog.dismiss()
        }
        closeButton.setOnClickListener {
            onClick()
            bottomSheetDialog.dismiss()
        }
    }
    bottomSheetDialog.setContentView(bottomSheetBinding.root)
    bottomSheetDialog.show()
}

fun Activity.showBottomSheet(
    titleDialog: Int? = null,
    titleButton: Int? = null,
    message: Int,
    onClick: () -> Unit = {}
) {
    val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialog)
    val bottomSheetBinding: BottomSheetBinding =
        BottomSheetBinding.inflate(layoutInflater, null, false)

    with(bottomSheetBinding) {
        textTitle.text = getString(titleDialog ?: R.string.text_title_bottom_sheet)
        textMessage.text = getText(message)
        btnClick.text = getString(titleButton ?: R.string.text_button_bottom_sheet)
        btnClick.setOnClickListener {
            onClick()
            bottomSheetDialog.dismiss()
        }
        closeButton.setOnClickListener {
            onClick()
            bottomSheetDialog.dismiss()
        }
    }
    bottomSheetDialog.setContentView(bottomSheetBinding.root)
    bottomSheetDialog.show()
}

fun Activity.showErrorSheet(
    titleDialog: Int? = null,
    titleButton: Int? = null,
    message: String,
    onClick: () -> Unit = {}
) {
    val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialog)
    val bottomSheetBinding: BottomSheetBinding =
        BottomSheetBinding.inflate(layoutInflater, null, false)

    with(bottomSheetBinding) {
        textTitle.text = getString(titleDialog ?: R.string.text_title_bottom_sheet)
        textMessage.text = message
        btnClick.text = getString(titleButton ?: R.string.text_button_bottom_sheet)
        btnClick.setOnClickListener {
            onClick()
            bottomSheetDialog.dismiss()
        }
        closeButton.setOnClickListener {
            onClick()
            bottomSheetDialog.dismiss()
        }
    }
    bottomSheetDialog.setContentView(bottomSheetBinding.root)
    bottomSheetDialog.show()
}