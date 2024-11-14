package com.example.featherbookbackend.library.application.commandservices;

import com.example.featherbookbackend.library.domain.model.entities.Book;
import com.example.featherbookbackend.library.infrastructure.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BookCommandService {

    @Autowired
    private BookRepository bookRepository;

    @Transactional
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    @Transactional
    public Book updateBook(String bookId, Book updatedBook) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isPresent()) {
            updatedBook.setId(bookId);
            return bookRepository.save(updatedBook);
        }
        return null;  // Implementar manejo de errores según sea necesario
    }

    @Transactional
    public void deleteBook(String bookId) {
        bookRepository.deleteById(bookId);
    }
}
