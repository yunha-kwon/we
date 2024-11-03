package com.data.datasource

import com.data.model.request.RequestSchedule
import com.data.model.response.ResponseSchedule

interface ScheduleDataSource {

    suspend fun getSchedule(
        year: Int,
        month: Int
    ): ResponseSchedule

    suspend fun postSchedule(
        requestSchedule: RequestSchedule
    ): ResponseSchedule.Schedule

    suspend fun patchScheduleToggle(
        scheduleId: Int
    ): ResponseSchedule.Schedule

    suspend fun deleteSchedule(scheduleId : Int) : Unit

    suspend fun patchSchedule(scheduleId : Int, requestSchedule: RequestSchedule) : ResponseSchedule.Schedule
}