package com.umc.study.service;

import com.umc.study.dto.BookResponse;
import com.umc.study.dto.CreateBookRequest;
import com.umc.study.entity.Book;
import com.umc.study.entity.Category;
import com.umc.study.exception.CustomException;
import com.umc.study.exception.ErrorCode;
import com.umc.study.repository.CategoryRepository;
import org.springframework.transaction.annotation.Transactional;
import com.umc.study.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<BookResponse> getBooks(String keyword) { //BookResponse로 반환하는 이유는 Controller가 Entity를 못보게 하기 위함
        List<Book> books;
        if (keyword == null)
            books = bookRepository.findAllByOrderByBookIdDesc();
        else
            books = bookRepository.findByTitleContaining(keyword);

        return books.stream()
                .map(BookResponse::from)
                .toList();
    }

    @Transactional
    public BookResponse createBook(CreateBookRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_CATEGORY_ID));
        if (bookRepository.existsByTitle(request.title())) {
            throw new CustomException(ErrorCode.DUPLICATE_TITLE);
        }
        Book book = new Book(category, request.title(), request.description());

        return BookResponse.from(bookRepository.save(book));
    }
}