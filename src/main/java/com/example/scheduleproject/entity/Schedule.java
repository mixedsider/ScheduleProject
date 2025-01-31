package com.example.scheduleproject.entity;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class Schedule {
    Long id;
    String todo;
    String author;
    String password;
    Timestamp createdAt;
    Timestamp updatedAt;
}
