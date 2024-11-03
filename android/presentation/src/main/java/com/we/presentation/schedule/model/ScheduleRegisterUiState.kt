package com.we.presentation.schedule.model

sealed interface ScheduleRegisterUiState {

    data object RegisterEmpty : ScheduleRegisterUiState

    data object RegisterSuccess : ScheduleRegisterUiState

    data class Error(val error: String) : ScheduleRegisterUiState

    data object UpdateSuccess : ScheduleRegisterUiState
}