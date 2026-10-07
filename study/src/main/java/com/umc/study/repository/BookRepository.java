package com.umc.study.repository;

import com.umc.study.entity.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

//@Repository // 스프링 컨테이너에 "나 창고지기 부품이야!"라고 등록
//@RequiredArgsConstructor
//public class BookRepository {
//
//    // 2단계에서 준비된 스프링의 DB 통신 도구(JdbcTemplate) 주입
//    private final JdbcTemplate jdbcTemplate;
//
//    public List<Map<String, Object>> findAll() {
//        String sql = "SELECT * FROM book";
//
//        // 쿼리를 실행하고 결과를 List<Map> 형태의 날것 데이터로 긁어옵니다.
//        // Map의 Key는 '컬럼명(title)', Value는 '실제 데이터(달빛 도서관)'가 됩니다.
//        return jdbcTemplate.queryForList(sql);
//    }
//
//    public void save(Map<String, Object> body){
//        // book_id는 AUTO_INCREMENT이므로 생략, is_available은 기본 true로 삽입
//        String sql = "INSERT INTO book (category_id, title, description, is_available) VALUES (?, ?, ?, true)";
//
//        // SQL 뒤에 파라미터를 차례대로 넘겨주면 ? 자리에 순서대로 안전하게 바인딩됩니다.
//        jdbcTemplate.update(
//                sql,
//                body.get("categoryId"),
//                body.get("title"),
//                body.get("description")
//        );
//    }
//
//    //특정 카테고리 도서 목록 조회 API 구현
//    public List<Map<String, Object>> findByCategory(Long categoryId){
//        String sql =  "SELECT * FROM book WHERE category_id = ?";
//        return jdbcTemplate.queryForList(sql, categoryId);
//    }
//}


public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByOrderByBookIdDesc();
    List<Book> findByTitleContainingOrderByBookIdDesc(String keyword);
    boolean existsByTitle(String title);
}
