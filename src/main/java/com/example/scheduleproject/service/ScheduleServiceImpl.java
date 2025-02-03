package com.example.scheduleproject.service;

import com.example.scheduleproject.dto.ScheduleRequestDto;
import com.example.scheduleproject.dto.ScheduleResponseDto;
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

@Service
@AllArgsConstructor
public class ScheduleServiceImpl implements ScheduleService{

    private final ScheduleRepository scheduleRepository;

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto dto) {

        Schedule schedule = createSchedule(dto.getTodo(), dto.getAuthor(), dto.getPassword());

        return scheduleRepository.saveSchedule(schedule);
    }

    @Override
    public List<ScheduleResponseDto> findAllSchedules() {
        return scheduleRepository.findAllSchedules();
    }

    @Override
    public List<ScheduleResponseDto> findSchedulesByAuthor(String author) {
        return scheduleRepository.findSchedulesByAuthor(author);
    }

    @Override
    public List<ScheduleResponseDto> findSchedulesByUpdatedAt(String updatedAt) {
        Date date;
        try {
            date = dateFormat.parse(updatedAt);

        } catch (ParseException e ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This is not the correct format.");
        }

        return scheduleRepository.findSchedulesByUpdatedAt(new Timestamp(date.getTime()));
    }

    @Override
    public List<ScheduleResponseDto> findSchedulesByAuthorAndUpdatedAt(String author, String updatedAt) {
        Date date;
        try {
            date = dateFormat.parse(updatedAt);

        } catch (ParseException e ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This is not the correct format.");
        }
        return scheduleRepository.findSchedulesByAuthorAndUpdatedAt(author, new Timestamp(date.getTime()));
    }

    @Override
    public ScheduleResponseDto findScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findScheduleByIdIrElseThrow(id);
        return new ScheduleResponseDto(schedule);
    }

    @Override
    @Transactional
    public ScheduleResponseDto patchSchedule(Long id, ScheduleRequestDto dto) {
        int updateRow = 0;
        Schedule schedule;

        if( dto.getAuthor() == null && dto.getTodo() == null) { // 수정할 데이터가 입력이 안된 경우
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request.");
        }

        // 데이터를 가져와서 비밀번호 비교
        isPasswordValid(id, dto.getPassword());


        if( dto.getAuthor() != null && dto.getTodo() != null) { // 작성자 & 할일 바꾸는 경우
            updateRow = scheduleRepository.updateSchedule(id, dto.getAuthor(), dto.getTodo());
        }
        else if ( dto.getAuthor() != null ) { // 작성자만 바꾸는 경우
            updateRow = scheduleRepository.updateAuthor(id, dto.getAuthor());
        }
        else { // 할일만 바꾸는 경우
            updateRow = scheduleRepository.updateTodo(id,  dto.getTodo());
        }

        if( updateRow == 0 ) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }

        schedule = scheduleRepository.findScheduleByIdIrElseThrow(id);

        return new ScheduleResponseDto(schedule);
    }

    @Override
    public void deleteSchedule(Long id, ScheduleRequestDto dto) {
        int deleteRow = 0;

        if( dto.getTodo() != null || dto.getAuthor() != null ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request.");
        }

        isPasswordValid(id, dto.getPassword());

        deleteRow = scheduleRepository.deleteSchedule(id);

        if( deleteRow == 0 ) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }
    }

    private Schedule createSchedule(String todo, String author, String password) {
        return Schedule.builder()
                .todo(todo)
                .author(author)
                .password(password)
                .build();
    }

    // 비밀번호 유효성 검사
    private void isPasswordValid(Long id, String password) {
        Schedule schedule = scheduleRepository.findScheduleByIdIrElseThrow(id);

        if( !schedule.getPassword().equals(password) ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request.");
        }
    }
}
