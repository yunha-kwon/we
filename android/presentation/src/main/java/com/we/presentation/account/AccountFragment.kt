package com.we.presentation.account

import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.we.presentation.R
import com.we.presentation.account.util.BankList
import com.we.presentation.account.viewmodel.AccountViewModel
import com.we.presentation.base.BaseFragment
import com.we.presentation.databinding.FragmentAccountBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class AccountFragment : BaseFragment<FragmentAccountBinding>(R.layout.fragment_account) {
    private val accountViewModel: AccountViewModel by hiltNavGraphViewModels(R.id.nav_graph)
    private val safeArgs: AccountFragmentArgs by navArgs()

    override fun initView() {

        accountViewModel.setChooseBank(BankList(0, ""))
        accountViewModel.setAccountNumber("")

        accountBottomSheetClickListener()
        initTransferClickListener()
        bankChooseComplete()
        accountInputComplete()
        btnActivateCheck()
        observeAccountAuthSuccess()
        observeAccountNextButton()
    }


    private fun btnActivateCheck() {
        binding.apply {
            combine(
                accountViewModel.accountNumber.flowWithLifecycle(viewLifecycleOwner.lifecycle),
                accountViewModel.chooseBank.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            ) { accountNumber, chooseBank ->
                Timber.d("accountNumber : $accountNumber chooseBank : ${chooseBank.bankName}")
                if (accountNumber.isNotBlank() && chooseBank.bankName.isNotBlank()) {
                    tvRegisterAccount.isEnabled = true
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    private fun accountInputComplete() {
        binding.apply {
            etAccountNumber.addTextChangedListener {
                accountViewModel.setAccountNumber(it.toString())
            }
        }
    }

    private fun bankChooseComplete() {
        viewLifecycleOwner.lifecycleScope.launch {
            accountViewModel.chooseBank.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .onEach {

                    if (it.bankName != "") {
                        binding.apply {
                            tvAccountBankComplete.text = it.bankName
                            tvInputAccount.visibility = View.VISIBLE
                            etAccountNumber.visibility = View.VISIBLE
                        }
                    }
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }

    }

    private fun initTransferClickListener() {
        binding.apply {
            tvRegisterAccount.setOnClickListener {
                accountViewModel.accountAuth()

            }

            ivAccountBack.setOnClickListener {
                navigatePopBackStack()
            }
        }
    }

    private fun observeAccountAuthSuccess() {
        accountViewModel.accountAuthSuccess.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                if (it) {
                    val data = safeArgs.inputType
                    Timber.tag("들어오는 타입").d("$data")
                    val action = if (data) {
                        R.id.action_accountFragment_to_accountTransferFragment
                    } else {
                        R.id.action_guest_accountFragment_to_accountTransferFragment
                    }
                    navigateDestination(action)
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeAccountNextButton() {
        accountViewModel.nextButton.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                binding.tvRegisterAccount.apply {
                    isSelected = it
                    isEnabled = it
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }


    private fun accountBottomSheetClickListener() {
        binding.apply {
            flChooseBank.setOnClickListener {
                Timber.tag("계좌 은행").d("${safeArgs.inputType}")
                val value = if(!safeArgs.modalType){
                    true
                }else{
                    safeArgs.inputType
                }
                val modal = AccountModalBottomSheet(value)
                modal.show(parentFragmentManager, modal.tag)
            }
        }
    }
}