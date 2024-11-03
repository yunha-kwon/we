package com.data.api

import com.data.model.request.RequestSchedule
import com.data.model.response.ResponseSchedule
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ScheduleApi {

    @GET("schedule/view")
    suspend fun getSchedule(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): ResponseSchedule

    @POST("schedule/create")
    suspend fun postSchedule(
        @Body requestSchedule: RequestSchedule
    ): ResponseSchedule.Schedule

    @PATCH("schedule/toggle/{scheduleId}")
    suspend fun patchScheduleToggle(
        @Path("scheduleId") scheduleId: Int
    ): ResponseSchedule.Schedule

    @DELETE("schedule/delete/{scheduleId}")
    suspend fun deleteSchedule(
        @Path("scheduleId") scheduleId: Int
    ): Unit

    @PATCH("schedule/update/{scheduleId}")
    suspend fun patchSchedule(
        @Path("scheduleId") scheduleId: Int,
        @Body requestSchedule: RequestSchedule
    ): ResponseSchedule.Schedule
}