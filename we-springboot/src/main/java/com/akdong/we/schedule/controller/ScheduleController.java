package com.akdong.we.schedule.controller;

import com.akdong.we.schedule.domain.ScheduleDto;
import com.akdong.we.schedule.domain.ScheduleList;
import com.akdong.we.schedule.domain.ScheduleRequest;
import com.akdong.we.schedule.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/schedule")
@Tag(name = "일정 API", description = "일정 관련 API")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/create")
    @Operation(summary = "일정 생성", description = "일정 생성")
    public ScheduleDto saveSchedule(@RequestBody ScheduleRequest schedule) {
        return scheduleService.saveSchedule(schedule);
    }

    @GetMapping("/view")
    @Operation(summary = "일정 조회", description = "년도와 월을 기준으로 일정 조회")
    public ScheduleList getScheduleByDate(
            @RequestParam("year") int year, @RequestParam("month") int month) {
        return scheduleService.getScheduleByDate(year,month);
    }

    @PatchMapping("update/{scheduleId}")
    @Operation(summary = "일정 수정", description = "일정 수정")
    public ResponseEntity<ScheduleDto> updateSchedule(
            @PathVariable long scheduleId,
            @RequestBody ScheduleRequest scheduleRequest) {
        return scheduleService.updateSchedule(scheduleId, scheduleRequest);
    }

    @PatchMapping("/toggle/{scheduleId}")
    @Operation(summary = "일정 완료 토글", description = "일정 완료 토글")
    public ResponseEntity<ScheduleDto> toggleSchedule(
            @PathVariable long scheduleId){
        return scheduleService.toggleSchedule(scheduleId);
    }

    @DeleteMapping("/delete/{scheduleId}")
    @Operation(summary = "일정 삭제", description = "일정 삭제")
    public void deleteSchedule(
            @PathVariable long scheduleId){
        scheduleService.deleteSchedule(scheduleId);
    }
}
