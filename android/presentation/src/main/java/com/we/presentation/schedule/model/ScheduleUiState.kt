package com.we.presentation.schedule.model

import com.we.model.ScheduleData
import java.time.LocalDate

sealed interface ScheduleUiState {
    data object Loading : ScheduleUiState
    data class CalendarSet(val date: LocalDate, val calendarItem: List<CalendarItem>) :
        ScheduleUiState

    data class ScheduleSuccess(val scheduleData: ScheduleData) : ScheduleUiState
}