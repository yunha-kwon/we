package com.data.mapper

import com.data.model.request.RequestSchedule
import com.data.model.response.ResponseSchedule
import com.we.model.ScheduleData
import com.we.model.ScheduleParam

fun ResponseSchedule.toEntity(): List<ScheduleData> {
    return this.scheduleList.map {
        ScheduleData(
            address = it.address,
            scheduleId = it.schedule_id,
            scheduledTime = it.scheduled_time,
            price = it.price,
            done = it.done,
            coupleId = it.couple_id,
            content = it.content
        )
    }
}

fun ResponseSchedule.Schedule.toScheduleEntity() : ScheduleData{
    return  ScheduleData(
        address = this.address,
        scheduleId = this.schedule_id,
        scheduledTime = this.scheduled_time,
        price = this.price,
        done = this.done,
        coupleId = this.couple_id,
        content = this.content
    )


}

fun ScheduleParam.toModel(): RequestSchedule {
    return RequestSchedule(
        content = this.content,
        address = this.address,
        scheduled_time = this.date,
        price = this.price,
        done = false
    )
}