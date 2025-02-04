package com.example.scheduleproject.dto;

import com.example.scheduleproject.entity.Author;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class AuthorResponseDto {
    private Long id;
    private String name;
    private String email;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public AuthorResponseDto(Author author) {
        this.id = author.getId();
        this.name = author.getAuthor();
        this.email = author.getEmail();
        this.createdAt = author.getCreatedAt();
        this.updatedAt = author.getUpdatedAt();
    }
}
