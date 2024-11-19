package com.featherbook.featherbookbackend.library.application.queryservices;

import com.featherbook.featherbookbackend.library.domain.model.entities.Book;
import com.featherbook.featherbookbackend.library.infrastructure.repository.BookRepository;
import org.springframework.stereotype.Service;
import com.featherbook.featherbookbackend.usermanagement.domain.model.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * BookQueryService
 * <p>
 *     Service responsible for handling queries related to the `Book` entity.
 * </p>
 */
@Service
public class BookQueryService {

    private final BookRepository bookRepository;

    /**
     * Constructor.
     *
     * @param bookRepository The repository for the `Book` entity.
     */
    public BookQueryService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Finds a book by ID.
     *
     * @param bookId The ID of the book to find.
     * @return The found book.
     */
    public Optional<Book> getBookById(String bookId) {
        return bookRepository.findById(bookId);
    }

    /**
     * Finds a book by name.
     *
     * @param name The name of the book to find.
     * @return The found book.
     */
    public List<Book> getAccessibleBooks(User user) {
        double accessPercentage = user.getSubscriptionLevel().getBookAccessPercentage();
        List<Book> allBooks = bookRepository.findAll();
        int limit;
        if (user.getRole().equals("ADMIN")) {
            limit = (int) Math.ceil(allBooks.size() * 1.0);
        } else {
            limit = (int) Math.ceil(allBooks.size() * accessPercentage);
        }
        return allBooks.stream()
                .limit(limit)
                .collect(Collectors.toList());
    }
}
