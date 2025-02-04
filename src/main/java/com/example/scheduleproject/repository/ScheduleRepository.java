package com.example.scheduleproject.repository;

import com.example.scheduleproject.dto.ScheduleResponseDto;
import com.example.scheduleproject.entity.Schedule;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface ScheduleRepository {

    // 일정 저장
    ScheduleResponseDto saveSchedule(Schedule schedule);

    // 모든 일정 조회
    Map<String, Object> findAllSchedules(int page, int size);

    // 사용자 모든 일정 조회
    Map<String, Object> findSchedulesByAuthor(String author, int page, int size);

    // 수정 시간 기준으로 조회
    Map<String, Object> findSchedulesByUpdatedAt(Timestamp date, int page, int size);

    // 사용자의 수정 시간 기준으로 조회
    Map<String, Object> findSchedulesByAuthorAndUpdatedAt(String author, Timestamp updatedAt, int page, int size);

    // 일정 아이디로 단건 조회
    Schedule findScheduleByIdOrElseThrow(Long id);

    // 일정 수정
    int updateTodo(Long scheduleId, Long authorId, String todo);

    // 일정 삭제
    int deleteSchedule(Long scheduleId, Long authorId);
}
