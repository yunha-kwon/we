package com.we.presentation.accountcheck.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.datasource.BankDataSource
import com.data.model.request.RequestAccountAuth
import com.data.model.request.RequestTransactionHistory
import com.data.repository.BankRepository
import com.data.util.ApiResult
import com.we.model.TransactionHistoryData
import com.we.presentation.account.util.BankList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AccountCheckViewModel @Inject constructor(
    private val bankRepository: BankRepository
) : ViewModel() {
    private val _transactionList = MutableStateFlow<List<TransactionHistoryData>>(mutableListOf())
    val transactionList: Flow<List<TransactionHistoryData>> get() = _transactionList

    private fun setTransactionList(list: List<TransactionHistoryData>) {
        _transactionList.update { list }
    }


    fun transactionListLoading(accountNo: String) {
        viewModelScope.launch {
            bankRepository.postTransactionHistoryList(RequestTransactionHistory(accountNo))
                .collectLatest {
                    when (it) {
                        is ApiResult.Success -> {
                            Timber.d("AccountAuth : success")
                            setTransactionList(it.data)
                        }

                        is ApiResult.Error -> {
                            Timber.d("AccountAuth : fail " + it.exception.message)
                        }
                    }
                }
        }
    }
}