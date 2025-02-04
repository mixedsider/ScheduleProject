package com.example.scheduleproject.service;

import com.example.scheduleproject.dto.AuthorRequestDto;
import com.example.scheduleproject.dto.AuthorResponseDto;
import com.example.scheduleproject.entity.Author;

public interface AuthorService {

    AuthorResponseDto join(AuthorRequestDto dto);

    AuthorResponseDto findAuthorById(Long id, AuthorRequestDto dto);

    Author findAuthorByNameAndPassword(String name, String password);

    String findAuthorNameById(Long id);

    AuthorResponseDto patchAuthor(Long id, AuthorRequestDto dto);

    void deleteAuthor(Long id, AuthorRequestDto dto);
}
