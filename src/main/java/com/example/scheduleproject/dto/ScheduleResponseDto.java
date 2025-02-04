package com.example.scheduleproject.dto;

import com.example.scheduleproject.entity.Schedule;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Builder
public class ScheduleResponseDto {
    private Long id;
    private String todo;
    @Setter
    private String author;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.todo = schedule.getTodo();
        this.createdAt = schedule.getCreatedAt();
        this.updatedAt = schedule.getUpdatedAt();
    }

    public ScheduleResponseDto(Long id, String todo, String author, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.todo = todo;
        this.author = author;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
