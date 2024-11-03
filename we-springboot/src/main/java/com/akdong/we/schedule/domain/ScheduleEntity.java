package com.akdong.we.schedule.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Timestamp;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "schedule")
public class ScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long schedule_id;

    @Column(nullable = false)
    private Long couple_id;

    @Column
    private String content;

    @Column
    private String address;

    @Column
    private Timestamp scheduled_time;

    @Column
    private long price;

    @Column
    @ColumnDefault("false")
    private boolean isDone;

    public ScheduleDto asDto()
    {
        return ScheduleDto
                .builder()
                .schedule_id(schedule_id)
                .couple_id(couple_id)
                .content(content)
                .address(address)
                .scheduled_time(scheduled_time)
                .price(price)
                .isDone(isDone)
                .build();
    }
}
