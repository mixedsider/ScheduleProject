package com.example.scheduleproject.service;

import com.example.scheduleproject.dto.ScheduleResponseDto;
import com.example.scheduleproject.dto.ScheduleRequestDto;

import java.util.List;
import java.util.Map;

public interface ScheduleService {

    ScheduleResponseDto saveSchedule(ScheduleRequestDto dto);

    Map<String, Object> findAllSchedules(int page, int size);

    Map<String, Object> findSchedulesByAuthor(String author, int page, int size);

    Map<String, Object> findSchedulesByUpdatedAt(String updatedAt, int page, int size);

    Map<String, Object> findSchedulesByAuthorAndUpdatedAt(String author, String updatedAt, int page, int size);

    ScheduleResponseDto findScheduleById(Long id);

    ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto dto);

    void deleteSchedule(Long id, ScheduleRequestDto dto);
}
