package com.example.scheduleproject.repository;

import com.example.scheduleproject.dto.ScheduleResponseDto;
import com.example.scheduleproject.entity.Schedule;
import com.example.scheduleproject.service.AuthorService;
import com.example.scheduleproject.service.AuthorServiceImpl;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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
import java.util.stream.Collectors;

@Repository
public class ScheduleRepositoryImpl implements ScheduleRepository {

    private final JdbcTemplate jdbcTemplate;

    public ScheduleRepositoryImpl(DataSource dataSource, AuthorService authorService) {
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
        paramaters.put("authorId", schedule.getAuthorId());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(paramaters));

        Schedule resultSchedule = findScheduleByIdOrElseThrow(key.longValue());

        return new ScheduleResponseDto(resultSchedule);
    }

    @Override
    public List<ScheduleResponseDto> findAllSchedules() {
        String query =
                "SELECT s.*, a.name FROM schedule s " +
                "JOIN author a ON s.authorId = a.id ";
        return jdbcTemplate.query(query, scheduleResponseDtoRowMapper());
    }

    @Override
    public List<ScheduleResponseDto> findSchedulesByAuthor(String author) {
        String query =
                "SELECT s.*, a.name FROM schedule s " +
                "JOIN author a ON s.authorId = a.id " +
                "WHERE a.name = ?";
        return jdbcTemplate.query(query, scheduleResponseDtoRowMapper(), author);
    }

    @Override
    public List<ScheduleResponseDto> findSchedulesByUpdatedAt(Timestamp date) {
        String query =
                "SELECT s.* FROM schedule s " +
                "JOIN author a ON s.authorId = a.id " +
                "WHERE DATE(s.updatedAt) <= DATE(?)" +
                "ORDER BY updatedAt DESC";
        return jdbcTemplate.query(query, scheduleResponseDtoRowMapper(), date);
    }

    @Override
    public List<ScheduleResponseDto> findSchedulesByAuthorAndUpdatedAt(String author, Timestamp updatedAt) {
        String query =
                "SELECT s.* FROM schedule s " +
                "JOIN author a ON s.authorId = a.id " +
                "WHERE a.name = ? AND DATE(s.updatedAt) <= DATE(?)" +
                "ORDER BY updatedAt DESC";
        return jdbcTemplate.query(query, scheduleResponseDtoRowMapper(), author, updatedAt);
    }

    @Override
    public Schedule findScheduleByIdOrElseThrow(Long id) {
        String query =
                "SELECT s.* FROM schedule s " +
                "WHERE s.id = ?";
        List<Schedule> result = jdbcTemplate.query(query, scheduleRowMapper(), id);
        return result.stream().findAny().orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exists id = " + id)
        );
    }

    @Override
    public int updateTodo(Long scheduleId, Long authorId, String todo) {
        return jdbcTemplate.update("UPDATE schedule SET todo = ? WHERE id = ? AND authorId = ?", todo, scheduleId, authorId);
    }

    @Override
    public int deleteSchedule(Long scheduleId, Long authorId) {
        return jdbcTemplate.update("DELETE FROM schedule WHERE id = ? AND authorId = ?", scheduleId, authorId);
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

    private RowMapper<ScheduleResponseDto> scheduleResponseDtoRowMapper() {
        return new RowMapper<ScheduleResponseDto>() {
            @Override
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ScheduleResponseDto(
                        rs.getLong("id"),
                        rs.getString("todo"),
                        rs.getString("author"),
                        rs.getTimestamp("createdAt"),
                        rs.getTimestamp("updatedAt")
                );
            }
        };
    }
}
