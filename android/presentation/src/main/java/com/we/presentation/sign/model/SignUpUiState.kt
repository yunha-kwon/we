package com.we.presentation.sign.model

sealed interface SignUpUiState {

    data object SignUpSuccess : SignUpUiState

    data object EasyPasswordSuccess : SignUpUiState

    data object SignUpLoading : SignUpUiState

    data object SignUpEmpty : SignUpUiState

    data class SignUpError(val error: String) : SignUpUiState
}