package com.featherbook.featherbookbackend.usermanagement.application.commandservices;

import com.featherbook.featherbookbackend.usermanagement.domain.exceptions.UserNotFoundException;
import com.featherbook.featherbookbackend.usermanagement.domain.model.entities.BookHistory;
import com.featherbook.featherbookbackend.usermanagement.domain.model.entities.UploadedBook;
import com.featherbook.featherbookbackend.usermanagement.domain.model.entities.User;
import com.featherbook.featherbookbackend.usermanagement.domain.model.enums.SubscriptionLevel;
import com.featherbook.featherbookbackend.usermanagement.infrastructure.repository.UserRepository;
import com.featherbook.featherbookbackend.usermanagement.interfaces.rest.resources.UserUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.featherbook.featherbookbackend.usermanagement.domain.exceptions.InvalidUsernameException;

import java.time.LocalDate;
import java.util.Optional;

/**
 * UserCommandService
 * <p>
 *     Service class that handles the business logic for user commands.
 * </p>
 */
@Service
public class UserCommandService {

    private final UserRepository userRepository;

    /**
     * Constructor
     * @param userRepository the repository for user entities
     */
    public UserCommandService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creates a new user.
     * @param user the user to create
     */
    @Transactional
    public void createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with this email already exists.");
        }
        if (user.getUsername().contains(" ")) {
            throw new InvalidUsernameException("Invalid username, should not contain spaces.");
        }

        userRepository.save(user);
    }

    /**
     * Deletes a user.
     * @param userId the ID of the user to delete
     */
    @Transactional
    public void deleteUser(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        userRepository.deleteById(userId);
    }

    /**
     * Updates the fields of a user.
     * @param userId the ID of the user to update
     * @param updates the updates to apply
     * @return the updated user
     */
    @Transactional
    public User updateUserFields(String userId, UserUpdateRequest updates) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        if (updates.getSubscriptionLevel() != null) {
            try {
                user.setSubscriptionLevel(SubscriptionLevel.valueOf(updates.getSubscriptionLevel()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid subscription level provided.");
            }
        }

        if (updates.getBooksHistory() != null) {
            updates.getBooksHistory().forEach(entry -> {
                Optional<BookHistory> existingHistory = user.getBookHistory().stream()
                        .filter(history -> history.getBookId().equals(entry.getIdBook()))
                        .findFirst();

                if (existingHistory.isEmpty()) {
                    BookHistory newHistory = new BookHistory();
                    newHistory.setBookId(entry.getIdBook());
                    newHistory.setLastTimeRead(LocalDate.now());
                    newHistory.setUser(user);
                    user.getBookHistory().add(newHistory);
                }
            });
        }

        if (updates.getUploadedBooks() != null) {
            updates.getUploadedBooks().forEach(entry -> {
                Optional<UploadedBook> existingBook = user.getUploadedBooks().stream()
                        .filter(uploaded -> uploaded.getBookId().equals(entry.getIdBook()))
                        .findFirst();

                if (existingBook.isEmpty()) {
                    UploadedBook newBook = new UploadedBook();
                    newBook.setBookId(entry.getIdBook());
                    newBook.setUploadedDate(LocalDate.now());
                    newBook.setUser(user);
                    user.getUploadedBooks().add(newBook);
                }
            });
        }

        if (updates.getSavedBooks() != null) {
            updates.getSavedBooks().forEach(bookId -> {
                if (!user.getSavedBooks().contains(bookId)) {
                    user.getSavedBooks().add(bookId);
                }
            });
        }

        return userRepository.save(user);
    }

}
