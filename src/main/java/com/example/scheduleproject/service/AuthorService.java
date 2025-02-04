package com.example.scheduleproject.service;

import com.example.scheduleproject.dto.AuthorRequestDto;
import com.example.scheduleproject.dto.AuthorResponseDto;
import com.example.scheduleproject.entity.Author;

public interface AuthorService {

    // 회원가입
    AuthorResponseDto join(AuthorRequestDto dto);

    // 회원 아이디 조회
    AuthorResponseDto findAuthorById(Long id, AuthorRequestDto dto);

    // 이름과 비밀번호로 회원 조회
    Author findAuthorByNameAndPassword(String name, String password);

    // 이름으로 회원 아이디 조회
    String findAuthorNameById(Long id);

    // 회원 정보 수정
    AuthorResponseDto patchAuthor(Long id, AuthorRequestDto dto);

    // 회원 탈퇴
    void deleteAuthor(Long id, AuthorRequestDto dto);
}
