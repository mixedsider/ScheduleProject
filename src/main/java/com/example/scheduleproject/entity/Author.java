package com.example.scheduleproject.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Author {
    @Id
    private Long id;
    private String author;
    @Email
    private String email;
    @NotNull
    private String password;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
