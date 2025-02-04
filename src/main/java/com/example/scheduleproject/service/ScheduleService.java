package com.example.scheduleproject.service;

import com.example.scheduleproject.dto.ScheduleRequestDto;
import com.example.scheduleproject.dto.ScheduleResponseDto;

import java.util.Map;

public interface ScheduleService {

    // 일정 저장
    ScheduleResponseDto saveSchedule(ScheduleRequestDto dto);

    // 일정 다건 조회
    Map<String, Object> findAllSchedules(int page, int size);

    // 작성자 기준 다건 조회
    Map<String, Object> findSchedulesByAuthor(String author, int page, int size);

    // 수정 일 기준 다건 조회
    Map<String, Object> findSchedulesByUpdatedAt(String updatedAt, int page, int size);

    // 작성자의 수정일 기준 다건 조회
    Map<String, Object> findSchedulesByAuthorAndUpdatedAt(String author, String updatedAt, int page, int size);

    // 일정 단건 조회
    ScheduleResponseDto findScheduleById(Long id);

    // 일정 수정
    ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto dto);

    //일정 삭제
    void deleteSchedule(Long id, ScheduleRequestDto dto);
}
