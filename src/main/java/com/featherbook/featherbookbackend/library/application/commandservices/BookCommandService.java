package com.featherbook.featherbookbackend.library.application.commandservices;


import com.featherbook.featherbookbackend.library.domain.exceptions.BookNotFoundException;
import com.featherbook.featherbookbackend.library.domain.exceptions.DuplicateBookException;
import com.featherbook.featherbookbackend.library.domain.exceptions.InvalidSubscriptionException;
import com.featherbook.featherbookbackend.library.domain.model.entities.Book;
import com.featherbook.featherbookbackend.library.domain.model.entities.Review;
import com.featherbook.featherbookbackend.library.infrastructure.repository.BookRepository;
import com.featherbook.featherbookbackend.library.interfaces.rest.resources.ReviewRequest;
import com.featherbook.featherbookbackend.usermanagement.domain.model.entities.User;
import com.featherbook.featherbookbackend.usermanagement.domain.model.enums.SubscriptionLevel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * BookCommandService
 * <p>
 *     Service responsible for handling commands related to the `Book` entity.
 * </p>
 */

@Service
public class BookCommandService {

    private final BookRepository bookRepository;

    /**
     * Constructor.
     *
     * @param bookRepository The repository for the `Book` entity.
     */
    public BookCommandService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Creates a new book if the user has sufficient privileges.
     *
     * @param book The book to create.
     * @param user The user performing the action.
     * @return The created book.
     * @throws DuplicateBookException if a book with the same name or URL already exists.
     * @throws IllegalArgumentException if the user is not allowed to publish books.
     */
    @Transactional
    public Book createBook(Book book, User user) {
        if (!Objects.equals(user.getRole(), "ADMIN")) {
            if (!user.getSubscriptionLevel().canPublishBooks()) {
                throw new InvalidSubscriptionException(
                        "User cannot publish books at their current subscription level.");
            }
        }

        if (bookRepository.findByNameOrBookUrl(book.getName(), book.getBookUrl()).isPresent()) {
            throw new DuplicateBookException("A book with the same name or URL already exists.");
        }

        return bookRepository.save(book);
    }

    /**
     * Deletes a book by ID if the user has admin privileges.
     *
     * @param bookId   The ID of the book to delete.
     * @param userRole The role of the user performing the action.
     * @throws BookNotFoundException if the book is not found.
     * @throws IllegalArgumentException if the user does not have admin privileges.
     */
    @Transactional
    public void deleteBook(String bookId, String userRole) {
        if (!userRole.equals("ADMIN")) {
            throw new InvalidSubscriptionException("User does not have permission to delete a book.");
        }

        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundException("Book not found with ID: " + bookId);
        }

        bookRepository.deleteById(bookId);
    }

    /**
     * Adds or updates a review for a book.
     *
     * @param bookId           The ID of the book to review.
     * @param userId           The ID of the user submitting the review.
     * @param reviewRequest    The review details.
     * @param subscriptionLevel The subscription level of the user.
     * @param role             The role of the user.
     * @return The updated book.
     * @throws BookNotFoundException if the book is not found.
     * @throws IllegalArgumentException if the user is not allowed to comment on books.
     */
    @Transactional
    public Book addOrUpdateReview(String bookId, String userId, ReviewRequest reviewRequest,
            SubscriptionLevel subscriptionLevel, String role) {
        if (!subscriptionLevel.canCommentAndRate() && !"ADMIN".equalsIgnoreCase(role)) {
            throw new IllegalArgumentException(
                    "You are not allowed to comment on books at your current subscription level.");
        }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + bookId));

        Review existingReview = book.getReviews().stream()
                .filter(review -> review.getIdUser().equals(userId))
                .findFirst()
                .orElse(null);

        if (existingReview != null) {
            existingReview.setComment(reviewRequest.getComment());
            existingReview.setScore(reviewRequest.getScore());
        } else {
            Review newReview = new Review();
            newReview.setIdUser(userId);
            newReview.setComment(reviewRequest.getComment());
            newReview.setScore(reviewRequest.getScore());
            book.getReviews().add(newReview);
        }

        double newTotalScore = book.getReviews().stream()
                .mapToInt(Review::getScore)
                .average()
                .orElse(0);
        BigDecimal roundedScore = BigDecimal.valueOf(newTotalScore).setScale(1, RoundingMode.HALF_UP);
        book.setTotalScore(roundedScore.doubleValue());

        return bookRepository.save(book);
    }
}
