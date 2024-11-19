package com.featherbook.featherbookbackend.usermanagement.application.queryservices;

import com.featherbook.featherbookbackend.usermanagement.domain.model.entities.User;
import com.featherbook.featherbookbackend.usermanagement.infrastructure.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * UserQueryService
 * <p>
 *     This class is responsible for handling queries related to the User entity.
 * </p>
 */
@Service
public class UserQueryService {

    private final UserRepository userRepository;

    /**
     * Constructor
     * @param userRepository UserRepository
     */
    public UserQueryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Find a user by its ID
     * @param userId String
     * @return Optional<User>
     */
    public Optional<User> findById(String userId) {
        return userRepository.findById(userId);
    }

    /**
     * Get all users
     * @return List<User>
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Find a user by its username
     * @param email String
     * @return Optional<User>
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Check if a user exists by its username
     * @param username String
     * @return boolean
     */
    public boolean existsByUsernameIgnoreCase(String username) {
        return userRepository.findByUsernameIgnoreCase(username).isPresent();
    }
}
