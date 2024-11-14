package com.example.featherbookbackend.usermanagement.application.commandservices;

import com.example.featherbookbackend.usermanagement.domain.model.entities.User;
import com.example.featherbookbackend.usermanagement.infrastructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserCommandService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Creates a new user in the system.
     *
     * @param user The user to create.
     * @return The created user.
     */
    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Updates an existing user in the system.
     *
     * @param userId The ID of the user to update.
     * @param updatedUser The updated user details.
     * @return The updated user, or null if the user doesn't exist.
     */
    @Transactional
    public User updateUser(String userId, User updatedUser) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            updatedUser.setId(userId);
            return userRepository.save(updatedUser);
        }
        return null;  // TODO: Replace with proper exception handling
    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId The ID of the user to delete.
     */
    @Transactional
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}
