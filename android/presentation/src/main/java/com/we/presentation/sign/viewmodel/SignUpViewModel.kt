package com.we.presentation.sign.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.repository.SignRepository
import com.data.util.ApiResult
import com.we.model.SignUpParam
import com.we.presentation.sign.model.SignUpUiState
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
class SignUpViewModel @Inject constructor(
    private val signRepository: SignRepository
) : ViewModel() {


    private val _signUpParam = MutableStateFlow<SignUpParam>(SignUpParam())
    val signUpParam: StateFlow<SignUpParam> get() = _signUpParam

    private val _easyPasswordType = MutableStateFlow<Boolean>(true)
    val easyPasswordType: StateFlow<Boolean> get() = _easyPasswordType


    fun addRemoveEasyPassword(type: Boolean, value: String = "") { //간편 비밀 번호 추가 삭제
        _signUpParam.update {
            it.copy(easyPassword =
            it.easyPassword.toMutableList().also { list ->
                if (type) list.add(value) else list.removeLast()
            }
            )
        }
    }

    fun addRemoveEasyPasswordCheck(type: Boolean, value: String = "") { //간편 비밀 번호 확인 추가 삭제
        _signUpParam.update {
            it.copy(easyPasswordCheck =
            it.easyPasswordCheck.toMutableList().also { list ->
                if (type) list.add(value) else list.removeLast()
            }
            )
        }
    }

    fun clearEasyPasswordAndCheck() { // 간편 비밀번호 초기화
        _easyPasswordType.value = true
        _signUpParam.update {
            it.copy(
                easyPassword = mutableListOf(),
                easyPasswordCheck = mutableListOf()
            )
        }
    }

    fun clearSignUpParam() { //Sign Param 초기화
        _signUpParam.update { SignUpParam.DEFAULT }
    }


    suspend fun isSignUpParamValid(param: SignUpParam) { // 회원가입 파라미터 유효성 검증
        _nextButtonActivate.emit(
            param.email.isNotEmpty() &&
                    param.emailName.isNotEmpty() &&
                    param.password.isNotEmpty() &&
                    param.nickname.isNotEmpty()
        )
    }


    val _nextButtonActivate = MutableSharedFlow<Boolean>(1)
    val nextButtonActivate: SharedFlow<Boolean> get() = _nextButtonActivate

    fun setEmail(email: String) {
        _signUpParam.update { it.copy(email = email) }
    }

    fun setEmailName(emailName: String) {
        _signUpParam.update { it.copy(emailName = emailName) }
    }

    fun setPassword(password: String) {
        _signUpParam.update { it.copy(password = password) }
    }

    fun setNickName(nickname: String) {
        _signUpParam.update { it.copy(nickname = nickname) }
    }

    fun setEasyPasswordType(type: Boolean) {
        _easyPasswordType.value = type
    }

    fun checkEasyPasswordEquals() {
        val easyPassword = signUpParam.value.easyPassword.joinToString("")
        val easyPasswordCheck = signUpParam.value.easyPasswordCheck.joinToString("")

        if (easyPassword.equals(easyPasswordCheck)) {
            setSignUpUiState(SignUpUiState.EasyPasswordSuccess)
        } else {
            setSignUpUiState(SignUpUiState.SignUpEmpty)
        }
    }


    private val _signUpUiState = MutableStateFlow<SignUpUiState>(SignUpUiState.SignUpEmpty)
    val signUpUiState: StateFlow<SignUpUiState> get() = _signUpUiState

    fun setSignUpUiState(value: SignUpUiState) {
        _signUpUiState.update { value }
    }

    fun signUp() {
        viewModelScope.launch {
            signRepository.postSignUp(signUpParam.value).collectLatest {
                when (it) {
                    is ApiResult.Success -> {
                        _signUpUiState.emit(SignUpUiState.SignUpSuccess)
                        clearSignUpParam()
                        Timber.tag("회원가입").d("성공")
                    }

                    is ApiResult.Error -> {
                        setSignUpUiState(SignUpUiState.SignUpError(it.exception.message.toString()))
                        clearEasyPasswordAndCheck()
                        Timber.tag("회원가입").d("실패 ${it.exception}")
                    }
                }
            }
        }
    }


}