package com.example.scheduleproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Time;
import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Author {
    Long id;
    String name;
    String email;
    String password;
    Timestamp createdAt;
    Timestamp updatedAt;
}
