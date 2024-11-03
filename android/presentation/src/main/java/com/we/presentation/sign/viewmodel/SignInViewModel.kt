package com.we.presentation.sign.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.repository.SignRepository
import com.data.util.ApiResult
import com.data.util.TokenProvider
import com.we.model.SignInParam
import com.we.presentation.sign.model.SignInUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signRepository: SignRepository,
    private val tokenProvider: TokenProvider
) : ViewModel() {


    private val _signInParam = MutableStateFlow<SignInParam>(SignInParam())
    val signInParam: StateFlow<SignInParam> get() = _signInParam

    fun setSignInParam(type: Boolean, value: String) {
        if (type) {
            _signInParam.update { it.copy(email = value) }
        } else {
            _signInParam.update { it.copy(password = value) }
        }
    }

    private val _signInUiState = MutableStateFlow<SignInUiState>(SignInUiState.SignInLoading)
    val signInUiState: StateFlow<SignInUiState> get() = _signInUiState

    fun setSignInUiState(state: SignInUiState) {
        _signInUiState.update { state }
    }

    fun singIn() {
        viewModelScope.launch {
            signRepository.postLogin(signInParam.value).collectLatest {
                when (it) {
                    is ApiResult.Success -> {
                        setSignInUiState(
                            SignInUiState.SignInSuccess(
                                it.data.coupleJoined,
                                it.data.priorAccount
                            )
                        )

                        Timber.tag("로그인").d("성공 ${it.data}")
                    }

                    is ApiResult.Error -> {
                        setSignInUiState(SignInUiState.SignInError(it.exception.toString()))
                        Timber.tag("로그인").d("에러 ${it.exception}")
                    }
                }
            }
        }
    }
}