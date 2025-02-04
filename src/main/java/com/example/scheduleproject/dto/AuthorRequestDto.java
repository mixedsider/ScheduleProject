package com.example.scheduleproject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AuthorRequestDto {

    private String name;

    @Email
    private String email;

    @NotNull
    private String password;
}
