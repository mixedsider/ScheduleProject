package com.example.scheduleproject.entity;

import com.example.scheduleproject.service.ScheduleService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
public class Schedule {
    Long id;
    Long authorId;
    String todo;
    Timestamp createdAt;
    Timestamp updatedAt;

    public Schedule(String todo, Long authorId) {
        this.todo = todo;
        this.authorId = authorId;
    }
}
