package com.example.scheduleproject.entity;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
public class Schedule {
    @Id
    private Long id;
    private Long authorId;
    @Size(max = 200)
    private String todo;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Schedule(String todo, Long authorId) {
        this.todo = todo;
        this.authorId = authorId;
    }
}
