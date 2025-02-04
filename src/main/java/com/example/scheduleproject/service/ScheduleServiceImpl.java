package com.example.scheduleproject.service;

import com.example.scheduleproject.dto.ScheduleRequestDto;
import com.example.scheduleproject.dto.ScheduleResponseDto;
import com.example.scheduleproject.entity.Author;
import com.example.scheduleproject.entity.Schedule;
import com.example.scheduleproject.repository.ScheduleRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ScheduleServiceImpl implements ScheduleService{

    private final ScheduleRepository scheduleRepository;
    private final AuthorService authorService;

    // 날짜 형식
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto requestDto) {

        Long authorId = isPasswordValid(requestDto.getAuthor(), requestDto.getPassword()); // 사용자, 비번으로 조회

        ScheduleResponseDto responseDto = scheduleRepository.saveSchedule(new Schedule(requestDto.getTodo(), authorId));
        responseDto.setAuthor(authorService.findAuthorNameById(authorId));

        return responseDto;
    }

    @Override
    public Map<String, Object> findAllSchedules(int page, int size) {
        return scheduleRepository.findAllSchedules(page, size);
    }

    @Override
    public Map<String, Object> findSchedulesByAuthor(String author, int page, int size) {
        return scheduleRepository.findSchedulesByAuthor(author, page, size);
    }

    @Override
    public Map<String, Object> findSchedulesByUpdatedAt(String updatedAt, int page, int size) {
        Date date;
        try {
            date = dateFormat.parse(updatedAt); // 날짜 형식으로 변경
        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This is not the correct format.");
        }

        return scheduleRepository.findSchedulesByUpdatedAt(new Timestamp(date.getTime()), page, size);
    }

    @Override
    public Map<String, Object> findSchedulesByAuthorAndUpdatedAt(String author, String updatedAt, int page, int size) {
        Date date;
        try {
            date = dateFormat.parse(updatedAt); // 날짜 형식으로 변경

        } catch (ParseException e ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This is not the correct format.");
        }
        return scheduleRepository.findSchedulesByAuthorAndUpdatedAt(author, new Timestamp(date.getTime()), page, size);
    }


    @Override
    public ScheduleResponseDto findScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findScheduleByIdOrElseThrow(id); // 일정 조회 및 예외 처리
        ScheduleResponseDto dto = new ScheduleResponseDto(schedule);
        dto.setAuthor(authorService.findAuthorNameById(schedule.getAuthorId()));
        return dto;
    }

    @Override
    @Transactional
    public ScheduleResponseDto updateSchedule(Long scheduleId, ScheduleRequestDto dto) {
        int updateRow = 0;
        Schedule schedule;

        if( dto.getTodo() == null) { // 수정할 데이터가 입력이 안된 경우
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request.");
        }

        // 데이터를 가져와서 비밀번호 비교
        Long authorId = isPasswordValid(dto.getAuthor(), dto.getPassword());

        updateRow = scheduleRepository.updateTodo(scheduleId, authorId, dto.getTodo());

        if( updateRow == 0 ) { // 수정된 것이 없다면
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + scheduleId);
        }

        schedule = scheduleRepository.findScheduleByIdOrElseThrow(scheduleId);

        return new ScheduleResponseDto(schedule);
    }

    @Override
    public void deleteSchedule(Long scheduleId, ScheduleRequestDto dto) {
        int deleteRow = 0;
        Long authorId;

        if( dto.getTodo() != null ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request.");
        }

        authorId = isPasswordValid(dto.getAuthor(), dto.getPassword()); // 사용자 아이디 가져오기 & 예외 처리

        deleteRow = scheduleRepository.deleteSchedule(scheduleId, authorId);

        if( deleteRow == 0 ) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + scheduleId);
        }
    }

    // 비밀번호 유효성 검사
    private Long isPasswordValid(String name, String password) {
        Author author = authorService.findAuthorByNameAndPassword(name, password);

        return author.getId();
    }
}
