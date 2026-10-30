// src/main/java/.../controller/BookController.java
package com.umc.study.controller;
import com.umc.study.service.BookService;
import com.umc.study.dto.BookResponse;
import com.umc.study.dto.CreateBookRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

@RestController // 1. "나는 데이터를 JSON으로 서빙하는 API 카운터야!"
@RequestMapping("/books") // 2. 이 컨트롤러로 들어오는 요청의 기본 주소는 /books
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // 3. HTTP GET 방식으로 /books 요청이 들어왔을 때 이 메서드가 실행됩니다.
    @GetMapping
    public List<BookResponse> getBooks(
            @RequestParam(name = "keyword", required = false) String title
    ) {
        if (title == null || title.isBlank()) {
            return bookService.getAllBooks();
        }
        return bookService.searchBooksByTitle(title);
    }

    @GetMapping("/category/{categoryId}")
    public List<Map<String, Object>> getBooksByCategoryId(
            @PathVariable("categoryId") Long categoryId
    ){
        return bookService.getBooksByCategoryId(categoryId);
    }

    // POST http://localhost:8080/books
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    // Spring과 Jackson이 DTO의 구조를 보고 객체를 만들어 전달
    public BookResponse createBook(@Valid @RequestBody CreateBookRequest request){
        return bookService.createBook(request);
    }
}