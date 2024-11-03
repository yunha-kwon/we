package com.we.presentation.accountcheck

import android.view.LayoutInflater
import android.view.ViewGroup
import com.we.presentation.R
import com.we.presentation.base.BaseBottomSheet
import com.we.presentation.databinding.DialogAccountCheckBinding
import com.we.presentation.databinding.DialogChooseBankBinding

class AccountCheckBottomSheet :
    BaseBottomSheet<DialogAccountCheckBinding>() {

        override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> DialogAccountCheckBinding
        get() = DialogAccountCheckBinding::inflate

    override fun setupViews() {


    }
}