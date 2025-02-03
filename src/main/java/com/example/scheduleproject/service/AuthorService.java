package com.example.scheduleproject.service;

import com.example.scheduleproject.dto.AuthorRequestDto;
import com.example.scheduleproject.dto.AuthorResponseDto;

public interface AuthorService {

    AuthorResponseDto join(AuthorRequestDto dto);

    AuthorResponseDto findAuthorById(Long id, AuthorRequestDto dto);

    AuthorResponseDto patchAuthor(Long id, AuthorRequestDto dto);

    void deleteAuthor(Long id, AuthorRequestDto dto);
}
