package com.example.scheduleproject.repository;

import com.example.scheduleproject.dto.ScheduleResponseDto;
import com.example.scheduleproject.entity.Schedule;

import java.sql.Timestamp;
import java.util.List;

public interface ScheduleRepository {

    ScheduleResponseDto saveSchedule(Schedule schedule);

    List<ScheduleResponseDto> findAllSchedules();

    List<ScheduleResponseDto> findSchedulesByAuthor(String author);

    List<ScheduleResponseDto> findSchedulesByUpdatedAt(Timestamp date);

    List<ScheduleResponseDto> findSchedulesByAuthorAndUpdatedAt(String author, Timestamp updatedAt);

    Schedule findScheduleByIdOrElseThrow(Long id);

    int updateTodo(Long scheduleId, Long authorId, String todo);

    int deleteSchedule(Long scheduleId, Long authorId);
}
