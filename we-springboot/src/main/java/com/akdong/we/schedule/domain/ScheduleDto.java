package com.akdong.we.schedule.domain;

import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ScheduleDto {

    private Long schedule_id;

    private Long couple_id;

    private String content;


    private String address;

    private Timestamp scheduled_time;

    private long price;

    private boolean isDone;

    public ScheduleEntity asEntity()
    {
        return ScheduleEntity
                .builder()
                .schedule_id(schedule_id)
                .couple_id(couple_id)
                .content(content)
                .address(address)
                .scheduled_time(scheduled_time)
                .isDone(isDone)
                .build();
    }
}
