package com.akdong.we.schedule.domain;

import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ScheduleRequest {

    private String content;
    private String address;

    private Timestamp scheduled_time;

    private long price;

    private boolean isDone;

    public ScheduleEntity asEntity(Long couple_id)
    {
        return asEntity(null, couple_id);
    }

    public ScheduleEntity asEntity(Long scheduledId,Long couple_id)
    {
        return ScheduleEntity
                .builder()
                .schedule_id(scheduledId)
                .couple_id(couple_id)
                .content(content)
                .address(address)
                .scheduled_time(scheduled_time)
                .isDone(isDone)
                .price(price)
                .build();
    }
}
