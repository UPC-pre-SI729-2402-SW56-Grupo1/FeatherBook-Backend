package com.featherbook.featherbookbackend.library.interfaces.rest.controller;

import com.featherbook.featherbookbackend.library.application.commandservices.BookCommandService;
import com.featherbook.featherbookbackend.library.application.queryservices.BookQueryService;
import com.featherbook.featherbookbackend.library.domain.model.entities.Book;
import com.featherbook.featherbookbackend.library.domain.exceptions.BookNotFoundException;
import com.featherbook.featherbookbackend.library.interfaces.rest.resources.CreateBookRequest;
import com.featherbook.featherbookbackend.library.interfaces.rest.resources.ReviewRequest;
import com.featherbook.featherbookbackend.shared.application.queryservices.AuthenticationService;
import com.featherbook.featherbookbackend.usermanagement.domain.model.entities.User;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * BookController
 * <p>
 *     This class handles all REST API endpoints related to the `Book` entity.
 * </p>
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookCommandService bookCommandService;

    private final BookQueryService bookQueryService;

    private final AuthenticationService authenticationService;

    public BookController(BookCommandService bookCommandService, BookQueryService bookQueryService, AuthenticationService authenticationService) {
        this.bookCommandService = bookCommandService;
        this.bookQueryService = bookQueryService;
        this.authenticationService = authenticationService;
    }

    /**
     * Creates a new book in the library.
     *
     * @param bookRequest A request containing the details of the book to create.
     * @return A ResponseEntity containing the created book and a 201 status, or 400 if the request is invalid.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or isAuthenticated()")
    @Operation(summary = "Add a new book", description = "Adds a new book to the library.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Details of the book to be created", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateBookRequest.class))))
    public ResponseEntity<Book> createBook(@RequestBody Book bookRequest) {
        Book book = new Book();
        book.setName(bookRequest.getName());
        book.setSummary(bookRequest.getSummary());
        book.setCategory(bookRequest.getCategory());
        book.setBookUrl(bookRequest.getBookUrl());

        book.setViews(new Random().nextInt(100001));
        book.setReviews(List.of());

        User currentUser = authenticationService.getCurrentUser();
        Book createdBook = bookCommandService.createBook(book, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    /**
     * Retrieves a book by its unique ID.
     *
     * @param bookId The unique ID of the book to retrieve.
     * @return A ResponseEntity containing the book if found, or a 404 status if not.
     */
    @Operation(summary = "Get book by id", description = "Retrieves a book by its id.")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{bookId}")
    public ResponseEntity<Object> getBookById(
            @Parameter(description = "ID of the book to retrieve", required = true) @PathVariable String bookId) {
        return bookQueryService.getBookById(bookId)
                .<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Book not found with ID: " + bookId));
    }

    /**
     * Deletes a book from the library.
     *
     * @param bookId The unique ID of the book to delete.
     * @return A ResponseEntity with a 204 status if successful, or 404 if the book is not found.
     */
    @Operation(summary = "Delete a book", description = "Deletes a book from the library.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{bookId}")
    public ResponseEntity<String> deleteBook(
            @Parameter(description = "ID of the book to delete", required = true) @PathVariable String bookId) {
        try {
            bookCommandService.deleteBook(bookId, "ADMIN");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Book deleted successfully.");
        } catch (BookNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found with ID: " + bookId);
        }
    }

    /**
     * Retrieves all books accessible by the current user's subscription level.
     *
     * @return A list of books accessible to the current user.
     */
    @Operation(summary = "Get available books", description = "Gets available books for subscription level.")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/accessible")
    public ResponseEntity<List<Book>> getAccessibleBooks() {
        User currentUser = authenticationService.getCurrentUser();
        List<Book> accessibleBooks = bookQueryService.getAccessibleBooks(currentUser);
        return ResponseEntity.ok(accessibleBooks);
    }

    /**
     * Adds or updates a review for a specific book.
     *
     * @param bookId        The ID of the book to review.
     * @param reviewRequest The details of the review to add or update.
     * @return A ResponseEntity containing the updated book and a success message.
     */
    @Operation(summary = "Add a review", description = "Adds a review for a book from the library.")
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{bookId}/reviews")
    public ResponseEntity<Object> addOrUpdateReview(
            @PathVariable String bookId,
            @Valid @RequestBody ReviewRequest reviewRequest) {

        User currentUser = authenticationService.getCurrentUser();

        Book updatedBook = bookCommandService.addOrUpdateReview(
                bookId,
                currentUser.getId(),
                reviewRequest,
                currentUser.getSubscriptionLevel(),
                currentUser.getRole());

        return ResponseEntity.ok(Map.of("message", "Review added/updated successfully", "book", updatedBook));
    }
}
