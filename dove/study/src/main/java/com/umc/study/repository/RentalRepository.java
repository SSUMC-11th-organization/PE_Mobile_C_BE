package com.umc.study.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class RentalRepository {

    private final JdbcTemplate jdbcTemplate;

    public void save(Map<String, Object> body) {
        String sql = "INSERT INTO rental (user_id, book_id, rented_at, due_at) VALUES (?, ?, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY))";

        // SQL 뒤에 파라미터를 차례대로 넘겨주면 ? 자리에 순서대로 안전하게 바인딩됩니다.
        jdbcTemplate.update(
                sql,
                body.get("userId"),
                body.get("bookId")
        );
    }
}
