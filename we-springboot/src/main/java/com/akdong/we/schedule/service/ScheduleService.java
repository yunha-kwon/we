package com.akdong.we.schedule.service;

import com.akdong.we.couple.entity.Couple;
import com.akdong.we.couple.repository.CoupleRepository;
import com.akdong.we.notification.service.FirebaseService;
import com.akdong.we.schedule.domain.ScheduleDto;
import com.akdong.we.schedule.domain.ScheduleEntity;
import com.akdong.we.schedule.domain.ScheduleList;
import com.akdong.we.schedule.domain.ScheduleRequest;
import com.akdong.we.schedule.repository.ScheduleRepository;
import com.akdong.we.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ScheduleService {
    private final CoupleRepository coupleRepository;
    private final ScheduleRepository scheduleRepository;
    private final FirebaseService firebaseService;

    public ScheduleDto saveSchedule(ScheduleRequest schedule)
    {
        return scheduleRepository
                .save(schedule.asEntity(Util.getCoupleIdFromJwt(coupleRepository)))
                .asDto();
    }

    public ScheduleList getScheduleByDate(int year,int month)
    {
        return ScheduleList
                .builder()
                .year(year)
                .month(month)
                .scheduleList(scheduleRepository
                        .getScheduleByDate(year,month, Util.getCoupleIdFromJwt(coupleRepository))
                        .stream()
                        .map(ScheduleEntity::asDto)
                        .toList()
                )
                .build();
    }

    public ResponseEntity<ScheduleDto> updateSchedule(long scheduleId,ScheduleRequest scheduleRequest)
    {
        return scheduleRepository
                .findById(scheduleId)
                .map(schedule -> new ResponseEntity<>(scheduleRepository
                        .save(scheduleRequest.asEntity(scheduleId,Util.getCoupleIdFromJwt(coupleRepository)))
                        .asDto(), HttpStatus.OK))
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "일정 " + scheduleId + "이 없습니다."));
    }

    public ResponseEntity<ScheduleDto> toggleSchedule(long scheduleId)
    {
        return scheduleRepository
                .findById(scheduleId)
                .map(schedule -> {
                    schedule.setDone(!schedule.isDone());
                    scheduleRepository.save(schedule);
                    return new ResponseEntity<>(schedule.asDto(), HttpStatus.OK);
                })
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "일정 " + scheduleId + "이 없습니다."));
    }

    public void deleteSchedule(long scheduleId)
    {
        scheduleRepository
                .findById(scheduleId)
                .ifPresent(scheduleRepository::delete);
    }

    @Scheduled(fixedRate = 3600000)
    public void checkSchedule() throws IOException{

        var scheduleList = scheduleRepository
                .findAll()
                .stream()
                .map(ScheduleEntity::asDto)
                .filter(it->isSameDay(it.getScheduled_time()))
                .toList();

        for (ScheduleDto scheduleDto : scheduleList) {
            try {
                sendScheduleMessage(scheduleDto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isSameDay(Timestamp timestamp) {
        if(timestamp == null) return false;
        // 현재 날짜
        LocalDate currentDate = LocalDate.now();
        // 타임스탬프의 날짜를 LocalDate로 변환
        LocalDate dateToCheck = timestamp.toLocalDateTime().toLocalDate();

        // 날짜가 동일한지 비교
        return currentDate.equals(dateToCheck);
    }

    public void sendScheduleMessage(ScheduleDto schedule) throws IOException {
        Couple couple = coupleRepository
                .findById(schedule.getCouple_id())
                .orElseThrow();

        firebaseService.sendMessageTo(couple.getMember1().getId(),
                "일정 알림",schedule.getContent() == null ? "" : schedule.getContent());
        firebaseService.sendMessageTo(couple.getMember2().getId(),
                "일정 알림",schedule.getContent() == null ? "" : schedule.getContent());
    }
}
