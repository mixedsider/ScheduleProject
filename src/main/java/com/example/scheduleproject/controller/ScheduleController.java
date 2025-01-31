package com.example.scheduleproject.controller;

import com.example.scheduleproject.dto.ScheduleRequestDto;
import com.example.scheduleproject.dto.ScheduleResponseDto;
import com.example.scheduleproject.service.ScheduleService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;


    @PostMapping("/schedules")
    public ResponseEntity<ScheduleResponseDto> saveSchedule(
            @RequestBody ScheduleRequestDto dto
            )
    {
        return new ResponseEntity<>(scheduleService.saveSchedule(dto), HttpStatus.CREATED);
    }

    @GetMapping("/schedules")
    public ResponseEntity<List<ScheduleResponseDto>> findAllSchedules() {
        return new ResponseEntity<>(scheduleService.findAllSchedules(), HttpStatus.OK);
    }
}
