package com.presentation.component.custom

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.presentation.component.adapter.CustomDropDownAdapter
import com.we.presentation.databinding.LayoutDropDownBinding
import com.we.presentation.R
import com.we.presentation.util.DropDownMenu

fun showCustomDropDownMenu(
    context: Context,
    view: View,
    dataList: List<DropDownData>,
    action: (DropDownData) -> Unit
) {

    val binding = LayoutDropDownBinding.inflate(LayoutInflater.from(context), null, false)


    val popup = PopupWindow(
        binding.root,
        RelativeLayout.LayoutParams.WRAP_CONTENT,
        RelativeLayout.LayoutParams.WRAP_CONTENT,
        true
    )

    val recyclerView: RecyclerView = binding.rcDropDown
    recyclerView.addItemDecoration(
        DividerItemDecoration(
            context,
            R.drawable.rectangle_gray_height_1
        )
    )

    val adapter = CustomDropDownAdapter()

    recyclerView.adapter = adapter

    adapter.submitList(dataList)

    adapter.setItemClickListener { data ->
        action.invoke(data)
        popup.dismiss()
    }

    popup.showAsDropDown(view, -250, 0, Gravity.END)
}

data class DropDownData(
    val content: String,
    val type: DropDownMenu
)