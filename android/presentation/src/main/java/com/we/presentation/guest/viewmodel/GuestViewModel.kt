package com.we.presentation.guest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.model.request.RequestRegisterPriorAccount
import com.data.repository.BankRepository
import com.data.repository.MemberRepository
import com.data.util.ApiResult
import com.we.model.BankData
import com.we.model.GetMemberData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GuestViewModel @Inject constructor(
    private val bankRepository: BankRepository,
    private val memberRepository: MemberRepository
) : ViewModel() {
    private val _accountList = MutableStateFlow<List<BankData>>(mutableListOf<BankData>())
    val accountList: StateFlow<List<BankData>> get() = _accountList

    init {
        getMembers()
    }

    fun getAccountList() {
        viewModelScope.launch {
            bankRepository.getMyAccount().collectLatest {
                when (it) {
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

    fun postPriorAccount(accountNo : String){
        val request = RequestRegisterPriorAccount(accountNo = accountNo)
        viewModelScope.launch {
            bankRepository.postPriorAccount(request).collectLatest {
                when(it){
                    is ApiResult.Success -> {
                        Timber.tag("대표 계좌 등록").d("bank load 성공 " + it.data)
                    }
                    is ApiResult.Error -> {
                        Timber.tag("대표 계좌 등록").d("bank load fail " + it.exception.message)
                    }
                }
            }
        }
    }

    private fun setAccountList(list: List<BankData>) {
        _accountList.update { list }
    }

    private val _members = MutableStateFlow<GetMemberData>(GetMemberData(""))
    val members: StateFlow<GetMemberData> get() = _members

    private fun getMembers(){
        viewModelScope.launch {
            memberRepository.getMembers().collectLatest {
                when(it){
                    is ApiResult.Success -> {
                        Timber.d("member load 성공 " + it.data)
                        setMembers(it.data)
                    }
                    is ApiResult.Error -> {
                        Timber.d("member load fail " + it.exception.message)
                    }
                }
            }
        }
    }

    private fun setMembers(member : GetMemberData){
        _members.update { member }
    }
}