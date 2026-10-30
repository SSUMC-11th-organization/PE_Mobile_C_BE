package com.umc.study.book.service;

import com.umc.study.book.dto.BookResponse;
import com.umc.study.book.dto.CreateBookRequest;
import com.umc.study.book.entity.Book;
import com.umc.study.book.repository.BookRepository;
import com.umc.study.category.entity.Category;
import com.umc.study.category.repository.CategoryRepository;
import com.umc.study.global.exception.BusinessException;
import com.umc.study.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<BookResponse> getBooks() {
        return bookRepository.findAllByOrderByBookIdDesc().stream()
                .map(BookResponse::from)
                .toList();
    }

    public BookResponse createBook(CreateBookRequest request) {
        if (bookRepository.existsByTitle(request.title())) {
            throw new BusinessException(ErrorCode.DUPLICATE_BOOK_TITLE);
        }

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        Book book = new Book(category, request.title(), request.description());
        return BookResponse.from(bookRepository.save(book));
    }

    //public List<Map<String, Object>> getBooksByCategory(Long category) {
    //    return bookRepository.findByCategoryId(category);
    //}
}