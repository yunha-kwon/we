package com.we.presentation.sign.model

interface SignInUiState {

    data class SignInSuccess(val coupleJoined: Boolean, val priorAccount : String?) : SignInUiState

    data class SignInError(val error: String) : SignInUiState

    data object SignInLoading : SignInUiState

}