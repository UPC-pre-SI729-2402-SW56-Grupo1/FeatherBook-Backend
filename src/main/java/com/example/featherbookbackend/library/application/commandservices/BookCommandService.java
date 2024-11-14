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

    /**
     * Creates a new book in the system.
     *
     * @param book The book to create.
     * @return The created book.
     */
    @Transactional
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    /**
     * Updates an existing book in the system.
     *
     * @param bookId The ID of the book to update.
     * @param updatedBook The updated book details.
     * @return The updated book, or null if the book doesn't exist.
     */
    @Transactional
    public Book updateBook(String bookId, Book updatedBook) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isPresent()) {
            updatedBook.setId(bookId);
            return bookRepository.save(updatedBook);
        }
        return null;  // TODO: Add appropriate error handling if the book doesn't exist
    }

    /**
     * Deletes a book by its ID.
     *
     * @param bookId The ID of the book to delete.
     */
    @Transactional
    public void deleteBook(String bookId) {
        bookRepository.deleteById(bookId);
    }
}
