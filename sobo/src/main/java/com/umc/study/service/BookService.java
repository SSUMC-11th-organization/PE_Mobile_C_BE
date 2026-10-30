// src/main/java/.../service/BookService.java
package com.umc.study.service;

import com.umc.study.dto.BookResponse;
import com.umc.study.repository.BookRawRepository;
import com.umc.study.repository.BookRepository;
import com.umc.study.dto.CreateBookRequest;
import com.umc.study.entity.Book;
import com.umc.study.entity.Category;
import com.umc.study.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRawRepository bookRawRepository; // 삭제할 예정
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public BookResponse createBook(CreateBookRequest request) {
        Category category = categoryRepository
                .findById(request.categoryId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "카테고리를 찾을 수 없습니다."
                ));

        if (bookRepository.existsByTitle(request.title())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "이미 존재하는 도서 제목입니다."
            );
        }

        Book book = new Book(
                category,
                request.title(),
                request.description()
        );
        Book savedBook = bookRepository.save(book);
        return BookResponse.from(savedBook);

    }

    @Transactional(readOnly = true)
    public List<BookResponse> getAllBooks() {
        return bookRepository.findAllByOrderByBookIdDesc()
                .stream()
                .map(BookResponse::from)
                .toList();
    }

    // Raw SQL 방식
    public List<Map<String, Object>> getBooksByCategoryId(Long categoryId) {
        return bookRawRepository.findByCategoryId(categoryId);
    }

    @Transactional(readOnly = true)
    public List<BookResponse> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContaining(title)
                .stream()
                .map(BookResponse::from)
                .toList();
    }
}



