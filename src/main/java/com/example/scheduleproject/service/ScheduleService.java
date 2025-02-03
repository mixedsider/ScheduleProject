package com.example.scheduleproject.service;

import com.example.scheduleproject.dto.ScheduleResponseDto;
import com.example.scheduleproject.dto.ScheduleRequestDto;

import java.util.List;

public interface ScheduleService {

    ScheduleResponseDto saveSchedule(ScheduleRequestDto dto);

    List<ScheduleResponseDto> findAllSchedules();

    List<ScheduleResponseDto> findSchedulesByAuthor(String author);

    List<ScheduleResponseDto> findSchedulesByUpdatedAt(String updatedAt);

    List<ScheduleResponseDto> findSchedulesByAuthorAndUpdatedAt(String author, String updatedAt);

    ScheduleResponseDto findScheduleById(Long id);
}
