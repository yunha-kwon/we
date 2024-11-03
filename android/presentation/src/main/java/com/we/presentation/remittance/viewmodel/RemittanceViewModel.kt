package com.we.presentation.remittance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.model.request.RequestTransfer
import com.data.repository.BankRepository
import com.data.repository.CoupleRepository
import com.data.util.ApiResult
import com.we.presentation.account.util.BankList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RemittanceViewModel @Inject constructor(
    private val bankRepository: BankRepository,
    private val coupleRepository: CoupleRepository
) : ViewModel() {

    private val _chooseBank = MutableStateFlow(BankList(0, ""))
    val chooseBank: Flow<BankList> get() = _chooseBank

    private val _accountNumber = MutableStateFlow("")
    val accountNumber: Flow<String> get() = _accountNumber

    private val _myAccountNumber = MutableStateFlow("")
    val myAccountNumber: Flow<String> get() = _myAccountNumber

    private val _money = MutableStateFlow("")
    val money: Flow<String> get() = _money


    fun setMoney(money: String) {
        _money.update { money }
    }

    fun setAccountNumber(number: String) {
        _accountNumber.update { number }
    }

    fun setMyAccountNumber(number: String) {
        _myAccountNumber.update { number }
    }

    fun setChooseBank(bank: BankList) {
        _chooseBank.update { bank }
    }

    fun postTransfer(type: Boolean, pin: String, ledgers: Int, onResult: (Boolean) -> Unit) {

        lateinit var request: RequestTransfer
        request = RequestTransfer(
            depositAccountNo = "",
            isBride = null,
            ledgerId = null,
            pin = pin,
            transactionBalance = 0,
            withdrawalAccountNo = ""
        )
        // withdrawalAccountNo 출금계좌
        // isBride = 신랑 신부
        // ledgerId = 장부
        // depositAccountNo 입금계좌
        // transactionBalance 거래금액
        // pin 간편 비밀 번호


        // type 이 true인 경우 일반 송금
        // type 이 false인 경우 축의금 송금

        Timber.d("postTransfer type : ${type}")

        if (type == true) {

            viewModelScope.launch {
                combine(
                    accountNumber,
                    money,
                    myAccountNumber
                ) { accountNumber, money, myAccountNumber ->
                    request = RequestTransfer(
                        depositAccountNo = accountNumber,
                        isBride = null,
                        ledgerId = null,
                        pin = pin,
                        transactionBalance = money.toInt(),
                        withdrawalAccountNo = myAccountNumber
                    )

                    Timber.d("postTransfer request : ${request}")
                    postTransferCallApi(request) {
                        when (it) {
                            true -> {
                                onResult(true)
                            }

                            false -> {
                                onResult(false)
                            }
                        }
                    }
                }.collect()
            }

        } else {
            viewModelScope.launch {
                combine(
                    accountNumber,
                    money,
                    myAccountNumber
                ) { accountNumber, money, myAccountNumber ->
                    request = RequestTransfer(
                        depositAccountNo = null,
                        isBride = null,
                        ledgerId = null,
                        pin = pin,
                        transactionBalance = money.toInt(),
                        withdrawalAccountNo = null
                    )

                    postTransferCallApi(request) {
                        when (it) {
                            true -> {
                                onResult(true)
                            }

                            false -> {
                                onResult(false)
                            }
                        }
                    }
                }.collect()
            }
        }
    }

    private fun postTransferCallApi(request: RequestTransfer, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            bankRepository.postTransfer(
                request
            )
                .collectLatest {
                    when (it) {
                        is ApiResult.Success -> {
                            Timber.d("Authcode : success ${it}")
                            onResult(true)
                        }

                        is ApiResult.Error -> {
                            Timber.d("Authcode : fail")
                            onResult(false)
                        }
                    }
                }
        }
    }

    private fun coupleCheck(){
        viewModelScope.launch {
            coupleRepository.getCouples().collectLatest {
                when (it){
                    is ApiResult.Success ->{

                    }
                    is ApiResult.Error -> {

                    }
                }
            }
        }
    }
}