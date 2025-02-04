package com.example.scheduleproject.exception;

import com.example.scheduleproject.entity.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.InputMismatchException;

@RestControllerAdvice
public class ExControllerAdvice {

    @ExceptionHandler(ResponseStatusException.class)
    public ErrorResult badPasswordExHandle(ResponseStatusException e) {
        return new ErrorResult("잘못된 입력입니다.", e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorResult nullPointExHandle(NullPointerException e) {
        return new ErrorResult("잘못된 참조 입니다.", e.getMessage());
    }

    @ExceptionHandler
    public ErrorResult deletePointExHandle(InputMismatchException e) {
        return new ErrorResult("이미 삭제된 참조 입니다.", e.getMessage());
    }
}
