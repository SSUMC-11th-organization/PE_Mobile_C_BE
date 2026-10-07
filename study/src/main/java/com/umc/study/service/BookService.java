package com.umc.study.service;

import com.umc.study.dto.BookResponse;
import com.umc.study.dto.CreateBookRequest;
import com.umc.study.entity.Book;
import com.umc.study.entity.Category;
import com.umc.study.exception.DuplicateBookException;
import com.umc.study.repository.BookRepository;
import com.umc.study.repository.CategoryRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

//@Service // 비즈니스 로직을 수행하는 메인 셰프 계층
//@RequiredArgsConstructor
//public class BookService {
//
//    // 창고지기(Repository)를 생성자 주입으로 데려옵니다.
//    private final BookRepository bookRepository;
//
//    public List<Map<String, Object>> getAllBooks() {
//        // 지금은 별도 가공 없이 창고지기가 가져온 도서 목록을 그대로 반환합니다.
//        return bookRepository.findAll();
//    }
//
//    public void createBook(Map<String, Object> body){
//        bookRepository.save(body);
//    }
//
//    //특정 카테고리 도서 목록 조회 API 구현
//    public List<Map<String, Object>> getBooksByCategory(Long categoryId) {
//        return bookRepository.findByCategory(categoryId);
//    }
//}

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<BookResponse> getBooks(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return bookRepository.findAllByOrderByBookIdDesc().stream()
                    .map(BookResponse::from)
                    .toList();
        }
        else {
            return bookRepository.findByTitleContainingOrderByBookIdDesc(keyword).stream()
                .map(BookResponse::from)
                .toList();
        }
    }

    @Transactional
    public BookResponse createBook(CreateBookRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        if (bookRepository.existsByTitle(request.title())){
            throw new DuplicateBookException("이미 등록된 도서입니다.");
        }
        Book book = new Book(category, request.title(), request.description());
        return BookResponse.from(bookRepository.save(book));
    }


}
