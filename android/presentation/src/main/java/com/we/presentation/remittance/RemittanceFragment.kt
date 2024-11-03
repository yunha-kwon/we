package com.we.presentation.remittance

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.we.presentation.R
import com.we.presentation.RemittanceGraghArgs
import com.we.presentation.account.AccountModalBottomSheet
import com.we.presentation.account.util.BankList
import com.we.presentation.base.BaseFragment
import com.we.presentation.databinding.FragmentRemittanceBinding
import com.we.presentation.remittance.viewmodel.RemittanceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class RemittanceFragment : BaseFragment<FragmentRemittanceBinding>(R.layout.fragment_remittance) {
    private val remittanceViewModel : RemittanceViewModel by hiltNavGraphViewModels(R.id.remittance_gragh)

    private val accountNo : RemittanceGraghArgs by navArgs()

    override fun initView() {

        // qr로 들어올 시 account 계정에 따라 처리하는 로직
        if(accountNo.account != null){
            remittanceViewModel.setMyAccountNumber(accountNo.account)
        }else{

        }

        initData()


        initClickListener()
        accountBottomSheetClickListener()
        chooseBank()
        accountNumberInput()
        moneyInput()
    }

    private fun initData(){
        remittanceViewModel.setChooseBank(BankList(0, ""))

    }

    private fun accountNumberInput(){
        binding.apply {
            etAccountNumber.addTextChangedListener {
                remittanceViewModel.setAccountNumber(it.toString())
            }
        }
    }

    private fun moneyInput(){
        binding.apply {
            etInputMoney.addTextChangedListener {
                remittanceViewModel.setMoney(it.toString())
            }
        }
    }

    private fun chooseBank(){
        binding.apply {
            remittanceViewModel.chooseBank.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .onEach{
                    tvAccountBankComplete.text = it.bankName
                    if (tvAccountBankComplete.text.isNotEmpty()){
                        tvInputAccount.visibility = View.VISIBLE
                        etAccountNumber.visibility = View.VISIBLE
                        tvInputMoney.visibility = View.VISIBLE
                        etInputMoney.visibility = View.VISIBLE
                        tvInputComplete.visibility = View.VISIBLE
                    }
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    private fun initClickListener(){
        binding.apply {
            ivRemittanceBack.setOnClickListener {
                navigatePopBackStack()
            }

            tvInputComplete.setOnClickListener {
                navigateDestination(R.id.action_remittanceFragment_to_remittanceCheckFragment)
            }
        }
    }

    private fun accountBottomSheetClickListener(){
        binding.apply {
            flChooseBank.setOnClickListener {
                val modal = AccountModalBottomSheet(false)
                modal.show(parentFragmentManager, modal.tag)
            }
        }
    }
}