package com.example.featherbookbackend.library.application.queryservices;

import com.example.featherbookbackend.library.domain.model.entities.Book;
import com.example.featherbookbackend.library.infrastructure.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookQueryService {

    @Autowired
    private BookRepository bookRepository;

    public Optional<Book> getBookById(String bookId) {
        return bookRepository.findById(bookId);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}
