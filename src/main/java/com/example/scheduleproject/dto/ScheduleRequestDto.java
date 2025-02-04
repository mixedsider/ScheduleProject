package com.example.scheduleproject.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ScheduleRequestDto {
    @Size(max = 200)
    private String todo;
    @NotNull
    private String author;
    @NotNull
    private String password;
}
