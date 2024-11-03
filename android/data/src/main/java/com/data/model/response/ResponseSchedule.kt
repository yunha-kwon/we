package com.data.model.response

data class ResponseSchedule(
    val month: Int,
    val scheduleList: List<Schedule>,
    val year: Int
) {
    data class Schedule(
        val address: String,
        val content: String,
        val couple_id: Int,
        val done: Boolean,
        val price: Long,
        val schedule_id: Int,
        val scheduled_time: String
    )
}