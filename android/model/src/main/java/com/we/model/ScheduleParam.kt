package com.we.model


data class ScheduleParam(
    val content: String = "",
    val address: String = "",
    val date: String = "",
    val price: Long = 0L,
    val done: Boolean = false
)