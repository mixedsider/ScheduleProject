package com.example.scheduleproject.repository;

import com.example.scheduleproject.dto.AuthorRequestDto;
import com.example.scheduleproject.dto.AuthorResponseDto;
import com.example.scheduleproject.entity.Author;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Repository
public class AuthorRepositoryImpl implements AuthorRepository{

    private final JdbcTemplate jdbcTemplate;

    public AuthorRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public AuthorResponseDto join(AuthorRequestDto dto) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert
                .withTableName("author") // 사용할 테이블
                .usingGeneratedKeyColumns("id") // 기본 키
                .usingColumns("name", "email", "password"); // 사용하는 컬럼

        //테이블에서 사용할 컬럼 입력
        Map<String, Object> paramaters = new HashMap<>();
        paramaters.put("name", dto.getName());
        paramaters.put("email",dto.getEmail());
        paramaters.put("password", dto.getPassword());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(paramaters));

        // 조회시 저장이 되어있다면 출력
        Author resultAuthor = findAuthorByIdOrElseThrow(key.longValue());

        return new AuthorResponseDto(resultAuthor);
    }

    @Override
    public Author findAuthorByIdOrElseThrow(Long id) {
        return jdbcTemplate.query("SELECT * FROM author WHERE id = ?", authorRowMapper(), id)
                .stream().findAny()
                .orElseThrow(() ->
                        //없는 데이터 참조 시
                        new NullPointerException("This is not the correct format.")
                );
    }

    @Override
    public Author findAuthorByNameAndPassword(String name, String password) {
        return jdbcTemplate
                .query("SELECT * FROM author WHERE name = ? AND password = ?", authorRowMapper(), name, password)
                .stream()
                .findAny()
                .orElseThrow( () ->
                        // 회원 정보 잘못 입력
                        new ResponseStatusException(HttpStatus.BAD_REQUEST, "Does not exist name = " + name)
                );
    }

    @Override
    public String findAuthorNameById(Long authorId) {
        Author author =  jdbcTemplate.query("SELECT * FROM author WHERE id = ?", authorRowMapper(), authorId)
                .stream()
                .findAny()
                .orElseThrow( () ->
                        // id에서 값이 없는 경우
                        new NullPointerException("Does not exist id = " + authorId)
                );

        return author.getAuthor();
    }

    @Override
    public int updateName(Long id, AuthorRequestDto dto) {
        return jdbcTemplate.update("UPDATE author SET name = ? WHERE id = ?", dto.getName(), id);
    }

    @Override
    public int updateEmail(Long id, AuthorRequestDto dto) {
        return jdbcTemplate.update("UPDATE author SET name = ? WHERE id = ?", dto.getEmail(), id);
    }

    @Override
    public int deleteAuthor(Long id) {
        return jdbcTemplate.update("DELETE FROM author WHERE id = ?", id);
    }

    private RowMapper<Author> authorRowMapper() {
        return new RowMapper<Author>() {
            @Override
            public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Author(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getTimestamp("createdAt"),
                        rs.getTimestamp("updatedAt")
                );
            }
        };
    }
}
