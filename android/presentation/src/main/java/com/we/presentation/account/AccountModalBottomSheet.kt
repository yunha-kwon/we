package com.we.presentation.account

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.we.presentation.R
import com.we.presentation.account.viewmodel.AccountViewModel
import com.we.presentation.base.BaseBottomSheet
import com.we.presentation.component.adapter.AccountBankChooseAdapter
import com.we.presentation.databinding.DialogChooseBankBinding
import com.we.presentation.remittance.viewmodel.RemittanceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class AccountModalBottomSheet(
    val type: Boolean
) : BaseBottomSheet<DialogChooseBankBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> DialogChooseBankBinding
        get() = DialogChooseBankBinding::inflate

    private val accountViewModel: AccountViewModel by hiltNavGraphViewModels(R.id.nav_graph)
    private val remittanceViewModel: RemittanceViewModel by hiltNavGraphViewModels(R.id.remittance_gragh)

    override fun setupViews() {
        val adapter = AccountBankChooseAdapter { item ->
            if (type) {
                accountViewModel.setChooseBank(item)
            } else {
                remittanceViewModel.setChooseBank(item)
            }
            if(type){
                accountViewModel.chooseBank.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                    .onEach { value ->
                        if (value.bankName.isNotEmpty()) {
                            Timber.d("item ${value.bankName} ${value.bankIcList}")
                            dismiss()
                        }
                    }
                    .launchIn(viewLifecycleOwner.lifecycleScope)
            }else{
                remittanceViewModel.chooseBank.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                    .onEach { value ->
                        if (value.bankName.isNotEmpty()) {
                            dismiss()
                        }
                    }
                    .launchIn(viewLifecycleOwner.lifecycleScope)
            }
        }

        binding.apply {
            rvChooseBank.adapter = adapter
            viewLifecycleOwner.lifecycleScope.launch {
                accountViewModel.bankList.collect { list ->
                    adapter.submitList(list)
                }
            }
        }
    }
}
