package com.example.scheduleproject.dto;

import com.example.scheduleproject.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class ScheduleResponseDto {
    Long id;
    String todo;
    String author;
    Timestamp createdAt;
    Timestamp updatedAt;

    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.todo = schedule.getTodo();
        this.author = schedule.getAuthor();
        this.createdAt = schedule.getCreatedAt();
        this.updatedAt = schedule.getUpdatedAt();
    }
}
