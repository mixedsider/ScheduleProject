package com.example.scheduleproject.controller;

import com.example.scheduleproject.dto.AuthorRequestDto;
import com.example.scheduleproject.dto.AuthorResponseDto;
import com.example.scheduleproject.service.AuthorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authors")
@AllArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping("/join")
    public ResponseEntity<AuthorResponseDto> join(@Valid @RequestBody AuthorRequestDto dto) {
        return new ResponseEntity<>(authorService.join(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponseDto> findAuthorById(
            @PathVariable Long id,
            @Valid @RequestBody AuthorRequestDto dto
    ) {
        return new ResponseEntity<>(authorService.findAuthorById(id, dto), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AuthorResponseDto> updateName(
            @PathVariable Long id,
            @Valid @RequestBody AuthorRequestDto dto
    ) {
        return new ResponseEntity<>(authorService.patchAuthor(id, dto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(
            @PathVariable Long id,
            @RequestBody AuthorRequestDto dto
    ) {
        authorService.deleteAuthor(id, dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
