package com.nikhilanand.library.service;


import com.nikhilanand.library.dto.BookRequestDTO;
import com.nikhilanand.library.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Book addBook(BookRequestDTO bookDto);
    List<Book> getAllBooks();
    Optional<Book> findBookById(String bookId);
    Optional<Book> findBookByTitle(String title);
    List<Book> searchBooksByPartialTitle(String title);
    Book updateBook(String bookId, BookRequestDTO bookDetails);
    void deleteBook(String bookId);
}