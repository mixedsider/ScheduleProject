package com.example.scheduleproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Author {
    Long id;
    String author;
    String email;
    String password;
    Timestamp createdAt;
    Timestamp updatedAt;
}
