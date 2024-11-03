package com.akdong.we.schedule.domain;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleList {
    private int year;
    private int month;

    private List<ScheduleDto> scheduleList;
}
