package com.example.scheduleproject.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ScheduleRequestDto {
    @Size(max = 200)
    String todo;
    @NotNull
    String author;
    @NotNull
    String password;
}
