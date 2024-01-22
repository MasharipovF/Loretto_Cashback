package com.example.lorettocashback.util

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import android.view.Window

object DialogSizeUtils {

    fun resizeDialog(dialog: Dialog, context: Context, widthPercent: Int?, heightPercent: Int?) {

        val width =
            if (widthPercent == null) ViewGroup.LayoutParams.WRAP_CONTENT else (context.resources.displayMetrics.widthPixels * widthPercent / 100)
        val height =
            if (heightPercent == null) ViewGroup.LayoutParams.WRAP_CONTENT else (context.resources.displayMetrics.heightPixels * heightPercent / 100)

        val window: Window? = dialog.window
        window?.setLayout(
            width,
            height
        )
    }


}