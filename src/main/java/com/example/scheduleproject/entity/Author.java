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
    Long id;
    String author;
    @Email
    String email;
    @NotNull
    String password;
    Timestamp createdAt;
    Timestamp updatedAt;
}
