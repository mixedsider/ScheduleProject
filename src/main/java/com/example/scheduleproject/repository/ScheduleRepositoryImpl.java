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

    public ScheduleRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    @Transactional
    public ScheduleResponseDto saveSchedule(Schedule schedule) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert
                .withTableName("schedule") // 사용할 테이블
                .usingGeneratedKeyColumns("id") // PK
                .usingColumns("todo", "authorId"); // 사용할 컬럼

        //테이블에서 사용할 컬럼 입력
        Map<String, Object> paramaters = new HashMap<>();
        paramaters.put("todo", schedule.getTodo());
        paramaters.put("authorId", schedule.getAuthorId());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(paramaters));

        // 조회시 저장이 되어있다면 출력
        Schedule resultSchedule = findScheduleByIdOrElseThrow(key.longValue());

        return new ScheduleResponseDto(resultSchedule);
    }

    @Override
    public Map<String, Object> findAllSchedules(int page, int size) {

        //페이징 필요한 정보
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
                new Object[]{size, offset}, //페이징 Object  :size offset
                scheduleResponseDtoRowMapper()
        );

        // 전체 레코드 수 조회
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
        //페이징 필요한 정보
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
                new Object[]{author, size, offset},//페이징 Object  :size offset
                scheduleResponseDtoRowMapper()
        );


        // 전체 레코드 수 조회
        int totalRecords = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM schedule s JOIN author a ON s.authorId = a.id WHERE a.name = ?",
                Integer.class, author
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
    public Map<String, Object> findSchedulesByUpdatedAt(Timestamp date, int page, int size) {
        //페이징 필요한 정보
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
                new Object[]{date, size, offset}, //페이징 Object  :size offset
                scheduleResponseDtoRowMapper()
        );

        // 전체 레코드 수 조회
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

        // 전체 레코드 수 조회
        int totalRecords = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM schedule s JOIN author a ON s.authorId = a.id WHERE a.name = ? AND DATE(s.updatedAt) <= DATE(?)",
                Integer.class, author, updatedAt
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
    public Schedule findScheduleByIdOrElseThrow(Long id) {
        // 전체 레코드 수 조회
        int totalRecords = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM schedule",
                Integer.class
        );
        if( totalRecords < id ) { // 레코드 보다 크게 입력을 해준 경우
            throw new InputMismatchException("이미 없거나 삭제된 입력입니다.");
        }
        String query =
                "SELECT s.* FROM schedule s " +
                "WHERE s.id = ?";
        List<Schedule> result = jdbcTemplate.query(query, scheduleRowMapper(), id); // 조회

        return result.stream().findAny().orElseThrow(() ->
                // 이미 삭제된 데이터 인 경우
                new InputMismatchException("Does not exists id = " + id)
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
