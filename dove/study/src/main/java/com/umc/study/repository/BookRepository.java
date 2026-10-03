package com.umc.study.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class BookRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> findAll() {
        String sql = "SELECT * FROM book";

        return jdbcTemplate.queryForList(sql);
    }
    public void save(Map<String, Object> body){
        String sql = "INSERT INTO book (category_id, title, description, is_available) VALUES (?, ?, ?, true)";

        jdbcTemplate.update(
                sql,
                body.get("categoryId"),
                body.get("title"),
                body.get("description")
        );
    }
    public List<Map<String, Object>> findBookByCategoryId(Long categoryId) {
        String sql = "SELECT * FROM book WHERE category_id = ?";

        return jdbcTemplate.queryForList(sql, categoryId);
    }
}