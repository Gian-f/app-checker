package com.br.appchecker.util

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import com.br.appchecker.R
import com.br.appchecker.databinding.BottomSheetBinding
import com.br.appchecker.databinding.LoadingBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

object LoadingUtils {

    fun showLoadingSheet(
        context: Context,
        titleDialog: Int? = null,
        message: Int? = null
    ): BottomSheetDialog {
        val bottomSheetDialog = BottomSheetDialog(context)
        val inflater = LayoutInflater.from(context)
        val loadingBottomSheetBinding = LoadingBottomSheetBinding.inflate(inflater, null, false)

        with(loadingBottomSheetBinding) {
            textTitle.text = context.getString(titleDialog ?: R.string.text_title_bottom_sheet)
            textMessage.text = context.getString(message ?: R.string.message_loading_bottom_sheet)
        }

        bottomSheetDialog.setContentView(loadingBottomSheetBinding.root)
        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.show()

        return bottomSheetDialog
    }

    fun dismissLoadingSheet(bottomSheetDialog: BottomSheetDialog?) {
        bottomSheetDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }

    fun showBottomSheet(
        context: Context,
        titleDialog: Int? = null,
        titleButton: Int? = null,
        message: Int,
        onClick: () -> Unit = {}
    ) {
        val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
        val inflater = LayoutInflater.from(context)
        val bottomSheetBinding: BottomSheetBinding =
            BottomSheetBinding.inflate(inflater, null, false)

        bottomSheetBinding.apply {
            textTitle.text = context.getString(titleDialog ?: R.string.text_title_bottom_sheet)
            textMessage.text = context.getText(message)
            btnClick.text = context.getString(titleButton ?: R.string.text_button_bottom_sheet)
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

    fun showErrorSheet(
        context: Context,
        titleDialog: Int? = null,
        titleButton: Int? = null,
        message: String,
        onClick: () -> Unit = {}
    ) {
        if (context is Activity && !context.isFinishing && !context.isDestroyed) {
            val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
            val inflater = LayoutInflater.from(context)
            val bottomSheetBinding: BottomSheetBinding =
                BottomSheetBinding.inflate(inflater, null, false)

            with(bottomSheetBinding) {
                textTitle.text = context.getString(titleDialog ?: R.string.text_title_bottom_sheet)
                textMessage.text = message
                btnClick.text = context.getString(titleButton ?: R.string.text_button_bottom_sheet)
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

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                if (bottomSheetDialog.isShowing) {
                    bottomSheetDialog.dismiss()
                }
            }, 2000)
        }
    }
}
