package com.example.scheduleproject.dto;

import com.example.scheduleproject.entity.Author;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class AuthorResponseDto {
    Long id;
    String name;
    String email;
    Timestamp createdAt;
    Timestamp updatedAt;

    public AuthorResponseDto(Author author) {
        this.id = author.getId();
        this.name = author.getName();
        this.email = author.getEmail();
        this.createdAt = author.getCreatedAt();
        this.updatedAt = author.getUpdatedAt();
    }
}
