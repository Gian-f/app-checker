package com.br.appchecker.util

import android.content.Context
import android.view.LayoutInflater
import com.br.appchecker.R
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
        bottomSheetDialog.show()

        return bottomSheetDialog
    }

    fun dismissLoadingSheet(bottomSheetDialog: BottomSheetDialog) {
        if (bottomSheetDialog.isShowing) {
            bottomSheetDialog.dismiss()
        }
    }
}