package com.example.scheduleproject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
public class Schedule {
    Long id;
    String todo;
    String author;
    String password;
    Timestamp createdAt;
    Timestamp updatedAt;
}
