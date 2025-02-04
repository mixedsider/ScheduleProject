package com.example.scheduleproject.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResult {
    //에러 리턴 값
    private String code;
    private String message;
}