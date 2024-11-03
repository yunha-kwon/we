package com.we.presentation.schedule.model

import android.os.Parcelable
import com.we.model.ScheduleData
import com.we.model.ScheduleParam
import kotlinx.parcelize.Parcelize


@Parcelize
data class ScheduleUpdateParam(
    val scheduleId : Int?,
    val content: String?,
    val address: String?,
    val date: String?,
    val price: Long?,
) : Parcelable

fun ScheduleData.toScheduleUpdateParam() : ScheduleUpdateParam{
    return ScheduleUpdateParam(
        scheduleId = this.scheduleId,
        content = this.content,
        address = this.address,
        date = this.scheduledTime,
        price = this.price
    )
}

fun ScheduleUpdateParam.toScheduleParam() : ScheduleParam{
    return ScheduleParam(
        content = this.content.toString(),
        address = this.address.toString(),
        date = this.date.toString(),
        price = this.price ?: 0L
    )
}

