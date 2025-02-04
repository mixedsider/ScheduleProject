package com.example.scheduleproject.repository;

import com.example.scheduleproject.dto.AuthorRequestDto;
import com.example.scheduleproject.dto.AuthorResponseDto;
import com.example.scheduleproject.entity.Author;

public interface AuthorRepository {

    // 회원가입
    AuthorResponseDto join(AuthorRequestDto dto);

    // 회원 아이디 조회
    Author findAuthorByIdOrElseThrow(Long id);

    // 이름과 비밀번호로 회원 조회
    Author findAuthorByNameAndPassword(String name, String password);

    // 이름으로 회원 아이디 조회
    String findAuthorNameById(Long authorId);

    // 회원 이름 업데이트
    int updateName(Long id, AuthorRequestDto dto);

    // 회원 이메일 수정
    int updateEmail(Long id, AuthorRequestDto dto);

    // 회원 탈퇴
    int deleteAuthor(Long id);
}
