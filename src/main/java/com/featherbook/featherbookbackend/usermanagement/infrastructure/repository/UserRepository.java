package com.featherbook.featherbookbackend.usermanagement.infrastructure.repository;

import com.featherbook.featherbookbackend.usermanagement.domain.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * Finds a user by their email.
     *
     * @param email The email of the user.
     * @return An Optional containing the user, if found.
     */
    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameIgnoreCase(String username);

    /**
     * Finds a user by their username.
     *
     * @param username The username.
     * @return Optional containing the user, if found.
     */
    Optional<User> findByUsername(String username);
}
