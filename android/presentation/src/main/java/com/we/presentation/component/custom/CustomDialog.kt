package com.we.presentation.component.custom

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.we.presentation.databinding.LayoutLoadingDialogBinding
import com.we.presentation.R

class CustomDialog(val context : Context) {
    val dialog = Dialog(context)



    fun progressDialog(): Dialog {

        val binding = DataBindingUtil.inflate<LayoutLoadingDialogBinding>(
            LayoutInflater.from(context),
            R.layout.layout_loading_dialog,
            null,
            false
        )
        dialog.setContentView(binding.root)
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
        dialog.setCancelable(false)     // 로딩창 꺼지지 않도록
        dialog.show()
        return dialog
    }
}