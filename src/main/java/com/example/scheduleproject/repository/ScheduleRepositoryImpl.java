package com.example.scheduleproject.repository;

import com.example.scheduleproject.dto.ScheduleResponseDto;
import com.example.scheduleproject.entity.Schedule;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ScheduleRepositoryImpl implements ScheduleRepository {

    private final JdbcTemplate jdbcTemplate;

    public ScheduleRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    @Transactional
    public ScheduleResponseDto saveSchedule(Schedule schedule) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert
                .withTableName("schedule")
                .usingGeneratedKeyColumns("id")
                .usingColumns("todo", "authorId");

        Map<String, Object> paramaters = new HashMap<>();
        paramaters.put("todo", schedule.getTodo());
        paramaters.put("author", schedule.getAuthorId());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(paramaters));

        Schedule resultSchedule = findScheduleByIdIrElseThrow(key.longValue());

        return new ScheduleResponseDto(resultSchedule);
    }

    @Override
    public List<ScheduleResponseDto> findAllSchedules() {
        return schedulesToResponseDtos(
                jdbcTemplate.query("SELECT * FROM schedule", scheduleRowMapper())
        );
    }

    @Override
    public List<ScheduleResponseDto> findSchedulesByAuthor(String author) {
        return schedulesToResponseDtos(
                jdbcTemplate.query("SELECT * FROM schedule WHERE author = ?", scheduleRowMapper(), author)
        );
    }

    @Override
    public List<ScheduleResponseDto> findSchedulesByUpdatedAt(Timestamp date) {
        return schedulesToResponseDtos(
                jdbcTemplate.query("SELECT * FROM schedule WHERE DATE(updatedAt) <= DATE(?)", scheduleRowMapper(), date)
        );
    }

    @Override
    public List<ScheduleResponseDto> findSchedulesByAuthorAndUpdatedAt(String author, Timestamp updated) {
        return schedulesToResponseDtos(
                jdbcTemplate.query("SELECT * FROM schedule WHERE author = ? AND DATE(updatedAt) <= DATE(?)", scheduleRowMapper(), author, updated)
        );
    }

    @Override
    public Schedule findScheduleByIdIrElseThrow(Long id) {
        List<Schedule> result = jdbcTemplate.query("SELECT * FROM schedule WHERE id = ?", scheduleRowMapper(), id);
        return result.stream().findAny().orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exists id = " + id)
        );
    }

    @Override
    public int updateTodo(Long id, String todo) {
        return jdbcTemplate.update("UPDATE schedule SET todo = ? WHERE id = ?", todo, id);
    }

    @Override
    public int updateAuthor(Long id, String author) {
        return jdbcTemplate.update("UPDATE schedule SET author = ? WHERE id = ?", author, id);
    }

    @Override
    public int updateSchedule(Long id, String author, String todo) {
        return jdbcTemplate.update("UPDATE schedule SET author = ?, todo = ? WHERE id = ?", author, todo, id);
    }

    @Override
    public int deleteSchedule(Long id) {
        return jdbcTemplate.update("DELETE FROM schedule WHERE id = ?", id);
    }

    private List<ScheduleResponseDto> schedulesToResponseDtos(List<Schedule> schedules) {
        return schedules.stream()
                .map(ScheduleResponseDto::new)
                .toList();
    }

    private RowMapper<Schedule> scheduleRowMapper() {
        return new RowMapper<Schedule>() {
            @Override
            public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Schedule(
                        rs.getLong("id"),
                        rs.getLong("authorId"),
                        rs.getString("todo"),
                        rs.getTimestamp("createdAt"),
                        rs.getTimestamp("updatedAt")
                );
            }
        };
    }
}
