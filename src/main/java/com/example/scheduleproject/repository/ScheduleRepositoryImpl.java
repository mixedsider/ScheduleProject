package com.example.scheduleproject.repository;

import com.example.scheduleproject.dto.ScheduleResponseDto;
import com.example.scheduleproject.entity.Schedule;
import com.example.scheduleproject.service.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;

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
    public Map<String, Object> findAllSchedules(int page, int size) {
        if(page < 1) {
            page = 1;
        }

        int offset = (page - 1) * size;

        String query =
                "SELECT s.*, a.name AS author " +
                "FROM schedule s " +
                "JOIN author a ON s.authorId = a.id " +
                "ORDER BY s.id " +
                "LIMIT ? OFFSET ?";

        List<ScheduleResponseDto> responseDtos = jdbcTemplate.query(
                query,
                new Object[]{size, offset},
                scheduleResponseDtoRowMapper()
        );

        // 전체 레코드 수 조회 (페이징에 필요한 정보)
        int totalRecords = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM schedule",
                Integer.class
        );

        // 전체 페이지 수 계산 (소수점 올림)
        int totalPages = (int) Math.ceil((double) totalRecords / size);

        // 결과 응답 Map 구성 (페이징 정보 포함)
        Map<String, Object> response = new HashMap<>();
        response.put("schedules", responseDtos);
        response.put("totalRecords", totalRecords);
        response.put("totalPages", totalPages);
        response.put("currentPage", page);
        response.put("pageSize", size);

        return response;
    }


    @Override
    public Map<String, Object> findSchedulesByAuthor(String author, int page, int size) {
        if(page < 1) {
            page = 1;
        }

        int offset = (page - 1) * size;
        String query =
                "SELECT s.*, a.name AS author FROM schedule s " +
                "JOIN author a ON s.authorId = a.id " +
                "WHERE a.name = ? " +
                "LIMIT ? OFFSET ?";

        List<ScheduleResponseDto> responseDtos = jdbcTemplate.query(
                query,
                new Object[]{author, size, offset},
                scheduleResponseDtoRowMapper()
        );

        int totalRecords = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM schedule s JOIN author a ON s.authorId = a.id WHERE a.name = ?",
                Integer.class, author
        );

        int totalPages = (int) Math.ceil((double) totalRecords / size);

        Map<String, Object> response = new HashMap<>();
        response.put("schedules", responseDtos);
        response.put("totalRecords", totalRecords);
        response.put("totalPages", totalPages);
        response.put("currentPage", page);
        response.put("pageSize", size);

        return response;
    }

    @Override
    public Map<String, Object> findSchedulesByUpdatedAt(Timestamp date, int page, int size) {
        if(page < 1) {
            page = 1;
        }

        int offset = (page - 1) * size;

        String query =
                "SELECT s.*, a.name AS author FROM schedule s " +
                "JOIN author a ON s.authorId = a.id " +
                "WHERE DATE(s.updatedAt) <= DATE(?)" +
                "ORDER BY updatedAt DESC " +
                "LIMIT ? OFFSET ?";

        List<ScheduleResponseDto> responseDtos = jdbcTemplate.query(
                query,
                new Object[]{date, size, offset},
                scheduleResponseDtoRowMapper()
        );

        int totalRecords = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM schedule",
                Integer.class
        );

        int totalPages = (int) Math.ceil((double) totalRecords / size);

        Map<String, Object> response = new HashMap<>();
        response.put("schedules", responseDtos);
        response.put("totalRecords", totalRecords);
        response.put("totalPages", totalPages);
        response.put("currentPage", page);
        response.put("pageSize", size);

        return response;
    }

    @Override
    public Map<String, Object> findSchedulesByAuthorAndUpdatedAt(String author, Timestamp updatedAt, int page, int size) {
        if(page < 1) {
            page = 1;
        }

        int offset = (page - 1) * size;

        String query =
                "SELECT s.*, a.name AS author FROM schedule s " +
                "JOIN author a ON s.authorId = a.id " +
                "WHERE a.name = ? AND DATE(s.updatedAt) <= DATE(?)" +
                "ORDER BY updatedAt DESC " +
                "LIMIT ? OFFSET ?";

        List<ScheduleResponseDto> responseDtos = jdbcTemplate.query(
                query,
                new Object[]{author, updatedAt, size, offset},
                scheduleResponseDtoRowMapper()
        );

        int totalRecords = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM schedule s JOIN author a ON s.authorId = a.id WHERE a.name = ? AND DATE(s.updatedAt) <= DATE(?)",
                Integer.class, author, updatedAt
        );

        int totalPages = (int) Math.ceil((double) totalRecords / size);

        Map<String, Object> response = new HashMap<>();
        response.put("schedules", responseDtos);
        response.put("totalRecords", totalRecords);
        response.put("totalPages", totalPages);
        response.put("currentPage", page);
        response.put("pageSize", size);

        return response;
    }

    @Override
    public Schedule findScheduleByIdOrElseThrow(Long id) {
        int totalRecords = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM schedule",
                Integer.class
        );
        if( totalRecords < id ) {
            throw new InputMismatchException("이미 없거나 삭제된 입력입니다.");
        }
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
