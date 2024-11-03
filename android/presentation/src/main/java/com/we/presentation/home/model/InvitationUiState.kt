package com.we.presentation.home.model

import com.we.model.InvitationData

sealed interface InvitationUiState {

    data class InvitationSuccess(val data: List<InvitationData>) : InvitationUiState
    data object InvitationError : InvitationUiState
    data object InvitationEmpty : InvitationUiState
}