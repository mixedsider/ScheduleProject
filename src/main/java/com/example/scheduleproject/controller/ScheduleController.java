package com.example.scheduleproject.controller;

import com.example.scheduleproject.dto.ScheduleRequestDto;
import com.example.scheduleproject.dto.ScheduleResponseDto;
import com.example.scheduleproject.service.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @PostMapping("/schedules")
    public ResponseEntity<ScheduleResponseDto> saveSchedule(
            @RequestBody ScheduleRequestDto dto
            )
    {
        return new ResponseEntity<>(scheduleService.saveSchedule(dto), HttpStatus.CREATED);
    }

    @GetMapping("/schedules")
    public ResponseEntity<List<ScheduleResponseDto>> findAllSchedules(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String updatedAt
    ) {
        List<ScheduleResponseDto> result;

        if(author != null && updatedAt != null) {
            result = scheduleService.findSchedulesByAuthorAndUpdatedAt(author, updatedAt);
        }
        else if(author != null) {
            result = scheduleService.findSchedulesByAuthor(author);
        }
        else if(updatedAt != null) {
            result = scheduleService.findSchedulesByUpdatedAt(updatedAt);
        }
        else {
            result = scheduleService.findAllSchedules();
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/schedules/{id}")
    public ResponseEntity<ScheduleResponseDto> findScheduleById(
            @PathVariable Long id
            )
    {
        return new ResponseEntity<>(scheduleService.findScheduleById(id), HttpStatus.OK);
    }
}
