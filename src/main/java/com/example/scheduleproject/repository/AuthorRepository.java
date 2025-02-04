package com.example.scheduleproject.repository;

import com.example.scheduleproject.dto.AuthorRequestDto;
import com.example.scheduleproject.dto.AuthorResponseDto;
import com.example.scheduleproject.entity.Author;

public interface AuthorRepository {

    AuthorResponseDto join(AuthorRequestDto dto);

    Author findAuthorByIdOrElseThrow(Long id);

    Author findAuthorByNameAndPassword(String name, String password);

    String findAuthorNameById(Long authorId);

    int updateName(Long id, AuthorRequestDto dto);

    int updateEmail(Long id, AuthorRequestDto dto);

    int deleteAuthor(Long id);
}
