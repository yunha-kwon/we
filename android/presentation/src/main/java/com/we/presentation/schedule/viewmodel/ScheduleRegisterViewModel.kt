package com.we.presentation.schedule.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.repository.ScheduleRepository
import com.data.util.ApiResult
import com.we.model.ScheduleParam
import com.we.presentation.schedule.model.ScheduleRegisterUiState
import com.we.presentation.util.ScheduleRegisterType
import com.we.presentation.util.toIso8601
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
class ScheduleRegisterViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {

    private val _scheduleRegisterUiState = MutableStateFlow<ScheduleRegisterUiState>(
        ScheduleRegisterUiState.RegisterEmpty
    )
    val scheduleRegisterUiState: StateFlow<ScheduleRegisterUiState> get() = _scheduleRegisterUiState

    val updateId = MutableStateFlow<Int>(0)

    fun setScheduleRegisterUiState(state: ScheduleRegisterUiState) {
        _scheduleRegisterUiState.update { state }
    }

    private val _scheduleRegisterParam = MutableStateFlow<ScheduleParam>(ScheduleParam())
    val scheduleRegisterParam: StateFlow<ScheduleParam> get() = _scheduleRegisterParam


    fun setAllRegisterParam(data: ScheduleParam, scheduleId: Int? = 0) {
        updateId.value = scheduleId ?: 0
        _scheduleRegisterParam.update { data }
    }

    fun setRegisterParam(type: ScheduleRegisterType, content: Any?) {
        when (type) {
            ScheduleRegisterType.DATE -> {
                Timber.tag("일정 시간").d("${content.toString().toIso8601()}")
                _scheduleRegisterParam.update {
                    it.copy(
                        date = content.toString().toIso8601() ?: ""
                    )
                }
            }

            ScheduleRegisterType.PRICE -> {
                if (content != null) {
                    _scheduleRegisterParam.update {
                        it.copy(
                            price = content as Long
                        )
                    }
                }
            }

            ScheduleRegisterType.CONTENT -> {
                _scheduleRegisterParam.update {
                    it.copy(
                        content = content.toString()
                    )
                }
            }

            ScheduleRegisterType.LOCATION -> {
                _scheduleRegisterParam.update {
                    it.copy(
                        address = content.toString()
                    )
                }
            }
        }
    }

    private val _registerButtonActive = MutableSharedFlow<Boolean>(1)
    val registerButtonActive: SharedFlow<Boolean> get() = _registerButtonActive

    fun setRegisterButtonActive() {
        viewModelScope.launch {
            _scheduleRegisterParam.collectLatest {
                val result =
                    it.content.isNotEmpty() && it.address.isNotEmpty() && it.date.isNotEmpty()
                _registerButtonActive.emit(result)
            }
        }
    }

    fun registerSchedule() {
        viewModelScope.launch {
            scheduleRepository.postSchedule(scheduleRegisterParam.value).collectLatest {
                when (it) {
                    is ApiResult.Success -> {
                        Timber.tag("일정 작성 성공").d("${it}")
                        setScheduleRegisterUiState(ScheduleRegisterUiState.RegisterSuccess)
                    }

                    is ApiResult.Error -> {
                        Timber.tag("일정 작성").d("${it.exception}")
                        setScheduleRegisterUiState(ScheduleRegisterUiState.Error(it.exception.toString()))
                    }
                }

            }
        }
    }

    fun updateSchedule(scheduleId: Int) {
        viewModelScope.launch {
            scheduleRepository.updateSchedule(scheduleId, scheduleRegisterParam.value)
                .collectLatest {
                    when (it) {
                        is ApiResult.Success -> {
                            Timber.tag("일정 수정 성공").d("${it}")
                            setScheduleRegisterUiState(ScheduleRegisterUiState.UpdateSuccess)
                        }

                        is ApiResult.Error -> {
                            Timber.tag("일정 작성").d("${it.exception}")
                            setScheduleRegisterUiState(ScheduleRegisterUiState.Error(it.exception.toString()))
                        }
                    }

                }
        }


    }

    init {
        setRegisterButtonActive()
    }
}