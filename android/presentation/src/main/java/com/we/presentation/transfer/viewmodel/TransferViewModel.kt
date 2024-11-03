package com.we.presentation.transfer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.model.request.RequestTransfer
import com.data.repository.BankRepository
import com.data.util.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class TransferViewModel @Inject constructor(
    private val bankRepository: BankRepository
) : ViewModel() {

    private val _accountNo = MutableStateFlow<String>("")
    val accountNo: StateFlow<String> get() = _accountNo

    fun setAccountNo(value: String) {
        _accountNo.update { value }
    }

    private val _easyPassword = MutableStateFlow<MutableList<String>>(mutableListOf())
    val easyPassword: StateFlow<List<String>> get() = _easyPassword


    private val _nextButton = MutableSharedFlow<Boolean>(1)
    val nextButton: SharedFlow<Boolean> get() = _nextButton

    private val _money = MutableStateFlow<String>("")
    val money: StateFlow<String> get() = _money

    fun setMoney(value: String) {
        viewModelScope.launch {
            _nextButton.emit(value.isNotEmpty())
        }
        _money.update { value }
    }

    private val _brideType = MutableStateFlow<Boolean>(true)
    val brideType: StateFlow<Boolean> get() = _brideType

    fun setBrideType(value: Boolean) {
        _brideType.update { value }
    }


    fun addRemoveEasyPassword(type: Boolean, value: String = "") { //간편 비밀 번호 추가 삭제
        _easyPassword.update {
            it.toMutableList().also { list ->
                if (type) list.add(value) else list.removeLast()
            }
        }
    }

    fun setBioEasyPassword(value: String) {
        _easyPassword.update {
            mutableListOf(value)
        }
    }

    fun clearEasyPasswordAndCheck() { // 간편 비밀번호 초기화
        _easyPassword.update {
            mutableListOf()
        }
    }

    private val _transferSuccess = MutableSharedFlow<Boolean>()
    val transferSuccess: SharedFlow<Boolean> get() = _transferSuccess

    fun postTransfer(ledgerId: Int) {
        val accountNo = if (accountNo.value.isNotEmpty()) {
            accountNo.value
        } else {
            null
        }
        viewModelScope.launch {
            Timber.tag("계좌 이체 데이터").d(
                "${
                    RequestTransfer(
                        ledgerId = ledgerId,
                        depositAccountNo = null,
                        transactionBalance = money.value.toInt(),
                        isBride = brideType.value,
                        pin = easyPassword.value.joinToString(""),
                        withdrawalAccountNo = accountNo
                    )
                }"
            )

            bankRepository.postTransfer(
                RequestTransfer(
                    ledgerId = ledgerId,
                    depositAccountNo = null,
                    transactionBalance = money.value.toInt(),
                    isBride = brideType.value,
                    pin = easyPassword.value.joinToString(""),
                    withdrawalAccountNo = accountNo
                )
            ).collectLatest {
                when (it) {
                    is ApiResult.Success -> {
                        _transferSuccess.emit(true)
                    }

                    is ApiResult.Error -> {
                        Timber.tag("송금 이체").d("${it.exception}")
                        _transferSuccess.emit(false)
                    }
                }
            }
        }

    }


}