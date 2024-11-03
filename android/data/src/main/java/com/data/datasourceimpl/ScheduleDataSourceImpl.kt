package com.data.datasourceimpl

import com.data.api.ScheduleApi
import com.data.datasource.ScheduleDataSource
import com.data.model.request.RequestSchedule
import com.data.model.response.ResponseSchedule
import javax.inject.Inject

class ScheduleDataSourceImpl @Inject constructor(
    private val scheduleApi: ScheduleApi
) : ScheduleDataSource {

    override suspend fun getSchedule(
        year: Int,
        month: Int
    ): ResponseSchedule {
        return scheduleApi.getSchedule(year, month)
    }

    override suspend fun postSchedule(requestSchedule: RequestSchedule): ResponseSchedule.Schedule {
        return scheduleApi.postSchedule(requestSchedule)
    }

    override suspend fun patchScheduleToggle(scheduleId: Int): ResponseSchedule.Schedule {
        return scheduleApi.patchScheduleToggle(scheduleId)
    }

    override suspend fun deleteSchedule(scheduleId: Int): Unit {
        return scheduleApi.deleteSchedule(scheduleId)
    }

    override suspend fun patchSchedule(
        scheduleId: Int,
        requestSchedule: RequestSchedule
    ): ResponseSchedule.Schedule {
        return scheduleApi.patchSchedule(scheduleId, requestSchedule)
    }
}