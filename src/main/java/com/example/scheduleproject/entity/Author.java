package com.example.scheduleproject.entity;

import lombok.Getter;

import java.sql.Time;
import java.sql.Timestamp;

@Getter
public class Author {
    Long id;
    String name;
    String email;
    String password;
    Timestamp createdAt;
    Timestamp updatedAt;
}
