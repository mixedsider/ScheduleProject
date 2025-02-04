package com.example.scheduleproject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AuthorRequestDto {
    String name;
    @Email
    String email;

    @NotNull
    String password;
}
