package com.example.scheduleproject.repository;

import com.example.scheduleproject.dto.ScheduleResponseDto;
import com.example.scheduleproject.entity.Schedule;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface ScheduleRepository {

    ScheduleResponseDto saveSchedule(Schedule schedule);

    Map<String, Object> findAllSchedules(int page, int size);

    Map<String, Object> findSchedulesByAuthor(String author, int page, int size);

    Map<String, Object> findSchedulesByUpdatedAt(Timestamp date, int page, int size);

    Map<String, Object> findSchedulesByAuthorAndUpdatedAt(String author, Timestamp updatedAt, int page, int size);

    Schedule findScheduleByIdOrElseThrow(Long id);

    int updateTodo(Long scheduleId, Long authorId, String todo);

    int deleteSchedule(Long scheduleId, Long authorId);
}
