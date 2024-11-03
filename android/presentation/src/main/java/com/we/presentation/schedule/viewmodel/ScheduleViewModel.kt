package com.we.presentation.schedule.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.repository.ScheduleRepository
import com.data.util.ApiResult
import com.we.model.ScheduleData
import com.we.presentation.schedule.model.CalendarItem
import com.we.presentation.schedule.model.ScheduleUiState
import com.we.presentation.util.CalendarType
import com.we.presentation.util.convertIsoToLocalDate
import com.we.presentation.util.toYearMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {

    val date = MutableStateFlow<LocalDate>(LocalDate.now())

    val selectedItem = MutableStateFlow<CalendarItem?>(null)

    fun setSelectedItem(item: CalendarItem) {
        selectedItem.update { item }
    }

    private val _scheduleUiState =
        MutableStateFlow<ScheduleUiState>(ScheduleUiState.CalendarSet(date.value, listOf()))
    val scheduleUiState: StateFlow<ScheduleUiState> get() = _scheduleUiState


    fun plusMinusMonth(type: Boolean) { // true -> plus, false -> minus
        if (type) {
            date.update {
                it.plusMonths(1)
            }
        } else {
            date.update {
                it.minusMonths(1)
            }
        }
    }

    private fun getSchedule(date: LocalDate) {
        viewModelScope.launch {
            val dateChange = date.toYearMonth()
            scheduleRepository.getSchedule(dateChange.first.toInt(), dateChange.second.toInt())
                .collectLatest {
                    when (it) {
                        is ApiResult.Success -> {
                            Timber.d("스케줄 불러오기 성공 ${it.data}")
                            _scheduleUiState.update { data ->
                                ScheduleUiState.CalendarSet(
                                    date, addDateList(
                                        date,
                                        it.data
                                    )
                                )
                            }
                        }

                        is ApiResult.Error -> {
                            Timber.d("스케줄 불러오기 실패 ${it.exception}")
                        }
                    }
                }
        }
    }


    fun addDateList(date: LocalDate, scheduleList: List<ScheduleData>): List<CalendarItem> {
        val yearMonth = YearMonth.from(date)
        val firstOfMonth = yearMonth.atDay(1)
        val daysInMonth = yearMonth.lengthOfMonth()
        val now = LocalDate.now()
        // 첫 번째 날의 요일 (1: 월요일, ..., 7: 일요일)
        val dayOfWeek = firstOfMonth.dayOfWeek.value

        // 이전 달 정보
        val previousMonth = yearMonth.minusMonths(1)
        val previousMonthDays = previousMonth.lengthOfMonth()


        val totalDays = 35

        // 이전 달의 날짜 수
        val daysFromPrevMonth = dayOfWeek % 7 // 일요일이면 0일 추가
        // 다음 달의 날짜 수
        val daysFromNextMonth = totalDays - daysFromPrevMonth - daysInMonth

        val days = mutableListOf<CalendarItem>()

        // 이전 달의 날짜 추가
        if (daysFromPrevMonth > 0) {
            val startPrevMonth = previousMonth.atDay(previousMonthDays - daysFromPrevMonth + 1)
            days += (0 until daysFromPrevMonth).map { i ->
                val currentDate = startPrevMonth.plusDays(i.toLong())
                CalendarItem(
                    date = currentDate,
                    calendarType = CalendarType.BEFORE,
                    listOf(),
                    false
                )
            }
        }

        // 현재 달의 날짜 추가
        days += (0 until daysInMonth).map { i ->
            val currentDate = firstOfMonth.plusDays(i.toLong())
            val currentSchedule =
                scheduleList.filter { currentDate.isEqual(it.scheduledTime?.convertIsoToLocalDate()) }
            val type = if (currentDate.isEqual(now)) CalendarType.TODAY else CalendarType.CURRENT
            val isSelected = selectedItem.value?.date == currentDate
            CalendarItem(
                date = currentDate,
                calendarType = type,
                currentSchedule,
                currentSchedule.isNotEmpty(),
                isSelected
            )
        }

        // 다음 달의 날짜 추가
        if (daysFromNextMonth > 0) {
            val nextMonth = yearMonth.plusMonths(1)
            days += (0 until daysFromNextMonth).map { i ->
                val currentDate = nextMonth.atDay(i + 1)
                CalendarItem(
                    date = currentDate,
                    calendarType = CalendarType.AFTER,
                    listOf(),
                    false
                )
            }
        }

        return days
    }

    fun clickDays(clickItem: CalendarItem) {
        val uiState = scheduleUiState.value
        if (uiState is ScheduleUiState.CalendarSet) {
            _scheduleUiState.update {
                ScheduleUiState.CalendarSet(
                    uiState.date,
                    uiState.calendarItem.map {
                        it.copy(
                            isSelected = it.date == clickItem.date
                        )
                    }
                )
            }
        }
    }

    fun findScheduleDate(calendarItem: List<CalendarItem>): List<ScheduleData> {
        val date = selectedItem.value?.date ?: LocalDate.now()
        return calendarItem.filter { it.date.isEqual(date) }.firstOrNull()?.schedule ?: listOf()

    }


    fun checkDate() {
        viewModelScope.launch {
            date.collectLatest { date ->
                getSchedule(date)
            }
        }
    }

    fun updateScheduleToggle(scheduleId: Int) {
        viewModelScope.launch {
            scheduleRepository.patchScheduleToggle(scheduleId).collectLatest {
                when (it) {
                    is ApiResult.Success -> {
                        checkDate()
                        Timber.tag("스케줄 확인 버튼").d("성공")
                    }

                    is ApiResult.Error -> {
                        Timber.tag("스케줄 확인 버튼").d("실패 ${it.exception}")
                    }
                }
            }


        }
    }

    fun deleteSchedule(scheduleId : Int){
        viewModelScope.launch{
            scheduleRepository.deleteSchedule(scheduleId).collectLatest {
                when(it){
                    is ApiResult.Success -> {
                        checkDate()
                    }
                    is ApiResult.Error -> {
                        Timber.tag("스케줄 삭제").d("실패 ${it.exception}")
                    }
                }
            }
        }
    }


}