package com.example.scheduleproject.controller;

import com.example.scheduleproject.dto.ScheduleRequestDto;
import com.example.scheduleproject.dto.ScheduleResponseDto;
import com.example.scheduleproject.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/schedules")
@AllArgsConstructor
public class ScheduleController {

    //DI
    private final ScheduleService scheduleService;

    //일정 저장
    @PostMapping("")
    public ResponseEntity<ScheduleResponseDto> saveSchedule(
            @Valid @RequestBody ScheduleRequestDto dto
    ) {
        return new ResponseEntity<>(scheduleService.saveSchedule(dto), HttpStatus.CREATED);
    }

    // 모든 일정 조회
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> findSchedules(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String updatedAt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Map<String, Object> result;

        if(author != null && updatedAt != null) {
            // 조회 기준 Author && updatedAt 으로 기준 검색
            result = scheduleService.findSchedulesByAuthorAndUpdatedAt(author, updatedAt, page, size);
        }
        else if(author != null) {
            // Author 기준 검색
            result = scheduleService.findSchedulesByAuthor(author, page, size);
        }
        else if(updatedAt != null) {
            // updatedAt 기준 검색
            result = scheduleService.findSchedulesByUpdatedAt(updatedAt, page, size);
        }
        else {
            // 조건 없이 모든 일정 검색
            result = scheduleService.findAllSchedules(page, size);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 단건 일정 조회
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findScheduleById(
            @PathVariable Long id
    ) {
        return new ResponseEntity<>(scheduleService.findScheduleById(id), HttpStatus.OK);
    }

    // 일정 업데이트
    @PatchMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            @PathVariable Long id,
            @Valid @RequestBody ScheduleRequestDto dto
    ) {
        return new ResponseEntity<>(scheduleService.updateSchedule(id, dto), HttpStatus.OK);
    }

    // 일정 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable Long id,
            @Valid @RequestBody ScheduleRequestDto dto
    ) {
        scheduleService.deleteSchedule(id, dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}