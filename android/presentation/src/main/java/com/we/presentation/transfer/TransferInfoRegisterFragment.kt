package com.we.presentation.transfer

import androidx.core.widget.addTextChangedListener
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.we.presentation.R
import com.we.presentation.base.BaseFragment
import com.we.presentation.databinding.FragmentTransferInfoRegisterBinding
import com.we.presentation.transfer.viewmodel.TransferViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class TransferInfoRegisterFragment :
    BaseFragment<FragmentTransferInfoRegisterBinding>(R.layout.fragment_transfer_info_register) {
    private val transferViewModel: TransferViewModel by hiltNavGraphViewModels(R.id.transfer_nav_graph)
    private val safeArgs : TransferInfoRegisterFragmentArgs by navArgs()

    override fun initView() {
        initData()
        setMoney()
        initClickEvent()
        observeNextButton()
    }

    private fun initData(){
        val data = safeArgs.accountNo
        if(!data.isNullOrEmpty()){
            transferViewModel.setAccountNo(data)
        }
    }


    private fun initClickEvent() {
        binding.apply {
            tvBride.isSelected = true
            tvBride.setOnClickListener {
                it.isSelected = true
                tvGroom.isSelected = false
                transferViewModel.setBrideType(true)
            }
            tvGroom.setOnClickListener {
                it.isSelected = true
                tvBride.isSelected = false
                transferViewModel.setBrideType(false)
            }
            tvInputComplete.setOnClickListener{
                navigateDestination(R.id.action_transferInfoRegisterFragment_to_transferEasyPasswordFragment)
            }
        }
    }

    private fun observeNextButton(){
        transferViewModel.nextButton.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                binding.tvInputComplete.apply {
                    isSelected = it
                    isEnabled = it
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }


    private fun setMoney() {
        binding.etInputMoney.addTextChangedListener {
            val data = it.toString()
            if (data.isNotEmpty()) {
                transferViewModel.setMoney(data)
            }
        }
    }

}