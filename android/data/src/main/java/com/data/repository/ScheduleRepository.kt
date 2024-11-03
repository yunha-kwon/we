package com.data.repository

import com.data.util.ApiResult
import com.we.model.ScheduleData
import com.we.model.ScheduleParam
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {

    fun getSchedule(year: Int, month: Int): Flow<ApiResult<List<ScheduleData>>>

    fun postSchedule(scheduleParam: ScheduleParam): Flow<ApiResult<ScheduleData>>

    fun patchScheduleToggle(scheduleId : Int) : Flow<ApiResult<ScheduleData>>

    fun deleteSchedule(scheduleId : Int) : Flow<ApiResult<Unit>>

    fun updateSchedule(scheduleId : Int, scheduleParam: ScheduleParam) : Flow<ApiResult<ScheduleData>>
}