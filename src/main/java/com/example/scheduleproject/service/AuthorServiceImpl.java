package com.example.scheduleproject.service;

import com.example.scheduleproject.dto.AuthorRequestDto;
import com.example.scheduleproject.dto.AuthorResponseDto;
import com.example.scheduleproject.entity.Author;
import com.example.scheduleproject.exception.ExControllerAdvice;
import com.example.scheduleproject.repository.AuthorRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService{

    private final AuthorRepository authorRepository;

    @Override
    public AuthorResponseDto join(AuthorRequestDto dto) {
        if ( // 유효성 검사
                dto.getName() != null && !dto.getName().isEmpty() &&
                        dto.getEmail() != null && !dto.getEmail().isEmpty() &&
                        dto.getPassword() != null && !dto.getPassword().isEmpty()
        ) {
            return authorRepository.join(dto);
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request");
        }
    }

    @Override
    public AuthorResponseDto findAuthorById(Long id, AuthorRequestDto dto) {

        Author author = authorRepository.findAuthorByIdOrElseThrow(id);
        if( author.getPassword().equals(dto.getPassword()) ) {
            return new AuthorResponseDto(author);
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request.");
        }
    }

    @Override
    public Author findAuthorByNameAndPassword(String name, String password) {
        return authorRepository.findAuthorByNameAndPassword(name, password);
    }

    @Override
    public String findAuthorNameById(Long id) {
        return authorRepository.findAuthorNameById(id);
    }

    @Override
    @Transactional
    public AuthorResponseDto patchAuthor(Long id, AuthorRequestDto dto) {
        int updateRow1 = 0;
        int updateRow2 = 0;

        Author author = findAuthorByNameAndPassword(dto.getName(), dto.getPassword());
        if( author.getId() != id ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 입력입니다.");
        }

        if( dto.getName() != null ) {
            updateRow1 = authorRepository.updateName(id, dto);
        }
        if( dto.getEmail() != null ) {
            updateRow2 = authorRepository.updateEmail(id, dto);
        }

        if( updateRow1 == 0 && updateRow2 == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }

        return new AuthorResponseDto(author);
    }

    @Override
    public void deleteAuthor(Long id, AuthorRequestDto dto) {
        Author author = authorRepository.findAuthorByIdOrElseThrow(id);

        if( !author.getPassword().equals(dto.getPassword()) ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request.");
        }
        authorRepository.deleteAuthor(id);
    }
}
