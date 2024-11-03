package com.we.presentation.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.repository.BankRepository
import com.data.util.ApiResult
import com.we.model.BankData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val bankRepository: BankRepository
) : ViewModel() {

    private val _accountList = MutableStateFlow<List<BankData>>(mutableListOf<BankData>())
    val accountList: Flow<List<BankData>> get() =  _accountList

    init {
        loadAccountList()
    }

    private fun setAccountList(list: List<BankData>) {
        _accountList.update { list }
    }

    private fun loadAccountList() {
        viewModelScope.launch {
            bankRepository.getMyAccountTest().collectLatest {
                when(it){
                    is ApiResult.Success -> {
                        setAccountList(it.data)
                        Timber.d("bank load 성공 " + it.data)
                    }
                    is ApiResult.Error -> {
                        Timber.d("bank load fail " + it.exception.message)
                    }
                }
            }
        }
    }

}