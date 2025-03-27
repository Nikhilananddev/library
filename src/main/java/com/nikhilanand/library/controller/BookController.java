package com.nikhilanand.library.controller;

import com.nikhilanand.library.dto.BookRequestDTO;
import com.nikhilanand.library.model.Book;
import com.nikhilanand.library.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Validated
public class BookController {
    @Autowired
   BookService bookService;

    @PostMapping
    public ResponseEntity<Book> addBook(@Valid @RequestBody BookRequestDTO bookDto) {
        if (!StringUtils.hasText(bookDto.getBookId())) {
            throw new IllegalArgumentException("Book ID is required and cannot be empty");
        }
        Book savedBook = bookService.addBook(bookDto);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            throw new IllegalArgumentException("No books found in the system.");
        }
        return ResponseEntity.ok(books);
    }

    @GetMapping("/id/{bookId}")
    public ResponseEntity<Book> getBookById(@PathVariable String bookId) {
        if (!StringUtils.hasText(bookId)) {
            throw new IllegalArgumentException("Book ID cannot be null or empty");
        }
        Optional<Book> bookOptional = bookService.findBookById(bookId);
        return bookOptional
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new IllegalArgumentException("No book found with ID: " + bookId));
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<Book> getBookByTitle(@PathVariable String title) {
        if (!StringUtils.hasText(title)) {
            throw new IllegalArgumentException("Book title cannot be null or empty");
        }
        Optional<Book> bookOptional = bookService.findBookByTitle(title);
        return bookOptional
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new IllegalArgumentException("No book found with title: " + title));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooksByTitle(@RequestParam String title) {
        if (!StringUtils.hasText(title)) {
            throw new IllegalArgumentException("Search title cannot be null or empty");
        }
        List<Book> books = bookService.searchBooksByPartialTitle(title);
        if (books.isEmpty()) {
            throw new IllegalArgumentException("No books found matching title: " + title);
        }
        return ResponseEntity.ok(books);
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<Book> updateBook(@PathVariable String bookId, @Valid @RequestBody BookRequestDTO bookDetails) {
        if (!StringUtils.hasText(bookId)) {
            throw new IllegalArgumentException("Book ID cannot be null or empty");
        }
        Book updatedBook = bookService.updateBook(bookId, bookDetails);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<String> deleteBook(@PathVariable String bookId) {
        if (!StringUtils.hasText(bookId)) {
            throw new IllegalArgumentException("Book ID cannot be null or empty");
        }
        bookService.deleteBook(bookId);
        return ResponseEntity.ok("Book with ID " + bookId + " deleted successfully.");
    }
}
