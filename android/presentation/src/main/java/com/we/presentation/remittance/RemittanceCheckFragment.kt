package com.we.presentation.remittance

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.we.presentation.R
import com.we.presentation.base.BaseFragment
import com.we.presentation.databinding.FragmentRemittanceCheckBinding
import com.we.presentation.remittance.viewmodel.RemittanceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RemittanceCheckFragment :
    BaseFragment<FragmentRemittanceCheckBinding>(R.layout.fragment_remittance_check) {
    private val remittanceViewModel: RemittanceViewModel by hiltNavGraphViewModels(R.id.remittance_gragh)
    override fun initView() {
        initClickListener()
        initStringSetting()
    }

    private fun initStringSetting() {
        binding.apply {
            lifecycleScope.launch {
                remittanceViewModel.money.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                    .onEach {
                        tvRemittanceSendMoney.text = it + "원"
                    }
                    .launchIn(viewLifecycleOwner.lifecycleScope)

                remittanceViewModel.accountNumber.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                    .onEach {
                        tvRemittanceSendAccount.text = it + "계좌로"
                    }
                    .launchIn(viewLifecycleOwner.lifecycleScope)
            }
        }
    }

    private fun initClickListener() {
        binding.apply {
            tvRemittanceCheck.setOnClickListener {
                navigateDestination(R.id.action_remittanceCheckFragment_to_easyPasswordRegisterFragment, bundleOf("easyPasswordType" to false))
            }

            ivRemittanceBack.setOnClickListener {
                navigatePopBackStack()
            }
        }
    }
}