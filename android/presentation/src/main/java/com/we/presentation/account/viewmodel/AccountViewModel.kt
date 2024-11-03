package com.we.presentation.account.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.model.request.RequestAccountAuth
import com.data.model.request.RequestAuthCode
import com.data.repository.BankRepository
import com.data.util.ApiResult
import com.we.presentation.account.util.BankList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val bankRepository: BankRepository
) : ViewModel() {


    private val _bankList = MutableStateFlow<List<BankList>>(mutableListOf())
    val bankList: Flow<List<BankList>> get() = _bankList

    private val _chooseBank = MutableStateFlow(BankList.DEFAULT)
    val chooseBank: Flow<BankList> get() = _chooseBank

    private val _accountNumber = MutableStateFlow("")
    val accountNumber: StateFlow<String> get() = _accountNumber

    private val _authCode = MutableStateFlow("")
    val authCode: Flow<String> get() = _authCode

    private val _nextButton = MutableSharedFlow<Boolean>(1)
    val nextButton: SharedFlow<Boolean> get() = _nextButton

    fun setNextButton() {
        combine(
            chooseBank,
            accountNumber
        ) { bank, account ->
            bank != BankList.DEFAULT && account.isNotEmpty()
        }.onEach {
            _nextButton.emit(it)
        }.launchIn(viewModelScope)

    }

    init {
        setBankList(BankList.bankLs)
        setChooseBank(BankList(0, ""))
        setAccountNumber("")
        setNextButton()
    }

    private fun setBankList(list: List<BankList>) {
        _bankList.update { list }
    }

    fun setChooseBank(bank: BankList) {
        _chooseBank.update { it.copy(bankName = bank.bankName, bankIcList = bank.bankIcList) }
    }

    fun setAccountNumber(number: String) {
        _accountNumber.update { number }
    }

    fun setAuthCode(code1: String, code2: String, code3: String, code4: String) {
        _authCode.update { code1 + code2 + code3 + code4 }
    }

    fun getAuthCodeCertified(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            Timber.d("Authcode : ${authCode.first()} ${accountNumber.first()}")
            bankRepository.checkAuthCode(
                RequestAuthCode(
                    accountNo = accountNumber.value,
                    authCode = authCode.first(),
                    authText = "akdong"
                )
            ).collectLatest {
                when (it) {
                    is ApiResult.Success -> {
                        Timber.d("Authcode : success ${it.data.Status}")
                        if (it.data.Status == "SUCCESS") {
                            onResult(true)
                        } else {
                            onResult(false)
                        }

                    }

                    is ApiResult.Error -> {
                        Timber.d("Authcode : fail")
                        onResult(false)
                    }
                }
            }
        }
    }

    private val _accountAuthSuccess = MutableSharedFlow<Boolean>()
    val accountAuthSuccess: SharedFlow<Boolean> get() = _accountAuthSuccess

    fun accountAuth() {
        viewModelScope.launch {
            bankRepository.accountAuth(RequestAccountAuth(accountNumber.first())).collect {
                when (it) {
                    is ApiResult.Success -> {
                        _accountAuthSuccess.emit(true)
                        Timber.d("AccountAuth : success")
                    }

                    is ApiResult.Error -> {
                        Timber.d("AccountAuth : fail ${it.exception}")
                    }
                }
            }
        }
    }

}