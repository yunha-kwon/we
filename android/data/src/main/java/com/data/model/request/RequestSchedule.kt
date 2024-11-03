package com.data.model.request

data class RequestSchedule(
    val content: String,
    val address: String,
    val scheduled_time: String,
    val price: Long,
    val done: Boolean
)
