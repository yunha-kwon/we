package com.we.presentation.transfer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.we.presentation.R
import com.we.presentation.base.BaseFragment
import com.we.presentation.databinding.FragmentTransferSuccessBinding
import timber.log.Timber

class TransferSuccessFragment : BaseFragment<FragmentTransferSuccessBinding>(R.layout.fragment_transfer_success) {

    private val args : TransferSuccessFragmentArgs by navArgs()

    override fun initView() {
//        Timber.tag("송금 결과").d("${args.result}")
        initReuslt(args.result)
    }

    fun initReuslt(result: Boolean) {
        binding.apply {
            when (result) {
                true -> {
                    remittanceFinish.text = "송금을 완료했어요!"
                    lottieRemittanceFail.visibility = View.GONE
                }

                false -> {
                    remittanceFinish.text = "송금을 실패했어요.."
                    lottieRemittanceSuccess.visibility = View.GONE
                }
            }

            tvRemittanceFinish.setOnClickListener {
                System.exit(0)
            }
        }
    }
}