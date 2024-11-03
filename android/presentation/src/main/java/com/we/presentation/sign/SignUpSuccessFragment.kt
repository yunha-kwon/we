package com.we.presentation.sign

import android.content.Intent
import com.we.presentation.R
import com.we.presentation.base.BaseFragment
import com.we.presentation.component.ShareData
import com.we.presentation.databinding.FragmentSignUpSuccessBinding
import com.we.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpSuccessFragment :
    BaseFragment<FragmentSignUpSuccessBinding>(R.layout.fragment_sign_up_success) {

    override fun initView() {
        initClickEvent()

    }

    private fun initClickEvent() {
        binding.apply {
            tvSignSuccessComplete.setOnClickListener {
                if (ShareData.transferType) {
                    startActivity(Intent(requireActivity(), MainActivity::class.java).apply {
                        putExtra("type", 4)
                    })
                } else {
                    navigatePopBackStack()
                }
            }
        }
    }
}