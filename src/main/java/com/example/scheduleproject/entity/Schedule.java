package com.example.scheduleproject.entity;

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
}
