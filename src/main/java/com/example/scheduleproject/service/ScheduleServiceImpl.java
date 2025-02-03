package com.example.scheduleproject.service;

import com.example.scheduleproject.dto.ScheduleRequestDto;
import com.example.scheduleproject.dto.ScheduleResponseDto;
import com.example.scheduleproject.entity.Schedule;
import com.example.scheduleproject.repository.ScheduleRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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

    private Schedule createSchedule(String todo, String author, String password) {
        return Schedule.builder()
                .todo(todo)
                .author(author)
                .password(password)
                .build();
    }
}
