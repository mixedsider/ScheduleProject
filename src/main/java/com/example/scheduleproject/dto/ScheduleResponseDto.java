package com.example.scheduleproject.dto;

import com.example.scheduleproject.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
@Builder
public class ScheduleResponseDto {
    Long id;
    String todo;
    @Setter
    String author;
    Timestamp createdAt;
    Timestamp updatedAt;

    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.todo = schedule.getTodo();
        this.createdAt = schedule.getCreatedAt();
        this.updatedAt = schedule.getUpdatedAt();
    }
}
