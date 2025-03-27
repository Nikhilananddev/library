package com.nikhilanand.library.service.impl;


import com.nikhilanand.library.dto.BookRequestDTO;
import global.AvailabilityStatus;
import com.nikhilanand.library.model.Book;
import com.nikhilanand.library.repositories.BookRepository;
import com.nikhilanand.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {


    @Autowired
    BookRepository bookRepository;

    @Override
    @Transactional
    public Book addBook(BookRequestDTO bookDto) {
        // Validate input
        if (bookDto == null) {
            throw new IllegalArgumentException("Book request cannot be null");
        }

        // Validate required fields
        validateBookRequestDTO(bookDto);

        // Check for existing book ID
        if (bookRepository.existsById(bookDto.getBookId())) {
            throw new IllegalArgumentException("Book with ID " + bookDto.getBookId() + " already exists");
        }

        // Convert DTO to Entity with null-safe defaults
        Book book = Book.builder()
                .bookId(bookDto.getBookId())
                .title(StringUtils.hasText(bookDto.getTitle()) ? bookDto.getTitle() : "Unknown Title")
                .author(StringUtils.hasText(bookDto.getAuthor()) ? bookDto.getAuthor() : "Unknown Author")
                .genre(bookDto.getGenre())
                .availabilityStatus(bookDto.getAvailabilityStatus() != null
                        ? bookDto.getAvailabilityStatus()
                        : AvailabilityStatus.AVAILABLE)
                .build();

        return bookRepository.save(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            throw new IllegalStateException("No books found in the library");
        }
        return books;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findBookById(String bookId) {
        if (!StringUtils.hasText(bookId)) {
            throw new IllegalArgumentException("Book ID cannot be null or empty");
        }
        return bookRepository.findByBookId(bookId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findBookByTitle(String title) {
        if (!StringUtils.hasText(title)) {
            throw new IllegalArgumentException("Book title cannot be null or empty");
        }
        return bookRepository.findByTitle(title);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> searchBooksByPartialTitle(String title) {
        if (!StringUtils.hasText(title)) {
            throw new IllegalArgumentException("Search title cannot be null or empty");
        }
        return bookRepository.searchBooksByPartialTitle(title);
    }

    @Override
    @Transactional
    public Book updateBook(String bookId, BookRequestDTO bookDetails) {
        // Validate inputs
        if (!StringUtils.hasText(bookId)) {
            throw new IllegalArgumentException("Book ID cannot be null or empty");
        }
        if (bookDetails == null) {
            throw new IllegalArgumentException("Book details cannot be null");
        }

        return bookRepository.findByBookId(bookId)
                .map(existingBook -> {
                    // Update with null-safe methods
                    existingBook.setTitle(StringUtils.hasText(bookDetails.getTitle())
                            ? bookDetails.getTitle()
                            : existingBook.getTitle());

                    existingBook.setAuthor(StringUtils.hasText(bookDetails.getAuthor())
                            ? bookDetails.getAuthor()
                            : existingBook.getAuthor());

                    existingBook.setGenre(bookDetails.getGenre() != null
                            ? bookDetails.getGenre()
                            : existingBook.getGenre());

                    existingBook.setAvailabilityStatus(bookDetails.getAvailabilityStatus() != null
                            ? bookDetails.getAvailabilityStatus()
                            : existingBook.getAvailabilityStatus());

                    return bookRepository.save(existingBook);
                })
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + bookId));
    }

    @Override
    @Transactional
    public void deleteBook(String bookId) {
        if (!StringUtils.hasText(bookId)) {
            throw new IllegalArgumentException("Book ID cannot be null or empty");
        }

        Book book = bookRepository.findByBookId(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + bookId));

        bookRepository.delete(book);
    }

    // Additional method for input validation
    private void validateBookRequestDTO(BookRequestDTO bookDto) {
        if (!StringUtils.hasText(bookDto.getBookId())) {
            throw new IllegalArgumentException("Book ID cannot be null or empty");
        }
    }
}