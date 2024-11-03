package com.we.presentation.accountcheck

import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.we.presentation.R
import com.we.presentation.account.AccountModalBottomSheet
import com.we.presentation.accountcheck.viewmodel.AccountCheckViewModel
import com.we.presentation.base.BaseFragment
import com.we.presentation.component.adapter.AccountCheckAdapter
import com.we.presentation.databinding.FragmentAccountCheckBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class AccountCheckFragment :
    BaseFragment<FragmentAccountCheckBinding>(R.layout.fragment_account_check) {

    private val accountCheckViewModel : AccountCheckViewModel by viewModels()

    private val args: AccountCheckFragmentArgs by navArgs()

    override fun initView() {
        initAdapter()
        initClickListener()
    }

    private fun initAdapter() {



        val account = args.account.split(" ")[0]
        val bankName = args.account.split(" ")[1]
        val remainMoney = args.account.split(" ")[2]

        Timber.d("account ${args.account}")


        accountCheckViewModel.transactionListLoading(account)

        val adapter = AccountCheckAdapter()
        val test = arrayListOf("1", "2", "3")

        binding.apply {
            rvAccountCheckList.adapter = adapter
            tvCheckAccountNumber.text = bankName + " " + account
            tvRemainMoney.text = "잔액 " + formatNumberWithCommas(remainMoney.toLong()) + "원"

        }


        accountCheckViewModel.transactionList.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                Timber.d("list $it")
                adapter.submitList(it)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

    }


    private fun initClickListener() {
        binding.apply {
            ivAccountBack.setOnClickListener {
                navigatePopBackStack()
            }

//            llAccountCheckMenu.setOnClickListener {
//                val modal = AccountCheckBottomSheet()
//                modal.show(parentFragmentManager, modal.tag)
//            }
        }
    }

    private fun formatNumberWithCommas(money: Long):String{
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        return numberFormat.format(money)
    }
}