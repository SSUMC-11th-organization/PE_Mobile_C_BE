package com.umc.study.book.controller;

import com.umc.study.book.dto.BookResponse;
import com.umc.study.book.dto.CreateBookRequest;
import com.umc.study.book.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public List<BookResponse> getBooks() {
        return bookService.getBooks();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse createBook(@Valid @RequestBody CreateBookRequest request) {
        return bookService.createBook(request);
    }

    //@GetMapping("/category/{categoryId}")
    //public List<Map<String, Object>> getBooksByCategory(@PathVariable Long categoryId) {
    //    return bookService.getBooksByCategory(categoryId);
    //}
}
