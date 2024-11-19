package com.featherbook.featherbookbackend.usermanagement.interfaces.rest.controller;

import com.featherbook.featherbookbackend.shared.application.queryservices.AuthenticationService;
import com.featherbook.featherbookbackend.usermanagement.application.commandservices.UserCommandService;
import com.featherbook.featherbookbackend.usermanagement.application.queryservices.UserQueryService;
import com.featherbook.featherbookbackend.usermanagement.domain.model.entities.User;
import com.featherbook.featherbookbackend.usermanagement.domain.exceptions.UserNotFoundException;
import com.featherbook.featherbookbackend.usermanagement.infrastructure.security.JwtUtil;
import com.featherbook.featherbookbackend.usermanagement.interfaces.rest.resources.UserLoginRequest;
import com.featherbook.featherbookbackend.usermanagement.interfaces.rest.resources.UserLoginResponse;
import com.featherbook.featherbookbackend.usermanagement.interfaces.rest.resources.UserRegistrationRequest;
import com.featherbook.featherbookbackend.usermanagement.interfaces.rest.resources.UserUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.featherbook.featherbookbackend.usermanagement.domain.exceptions.InvalidUsernameException;
import com.featherbook.featherbookbackend.usermanagement.domain.model.enums.SubscriptionLevel;
import com.featherbook.featherbookbackend.usermanagement.interfaces.rest.dto.UserResponse;
import com.featherbook.featherbookbackend.usermanagement.interfaces.rest.mapper.UserMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.validation.Valid;

/**
 * UserController
 * <p>
 *     This class handles all REST API endpoints related to the `User` entity.
 * </p>
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserCommandService userCommandService;

    private final UserQueryService userQueryService;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    private final AuthenticationService authenticationService;

    public UserController(UserCommandService userCommandService, UserQueryService userQueryService,
                          PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationService authenticationService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationService = authenticationService;
    }


    /**
     * Registers a new user with the provided details.
     *
     * @param registrationRequest A request containing the details of the user to register.
     * @return A ResponseEntity containing a message and a 201 status, or 400 if the request is invalid.
     */
    @Operation(summary = "Register a new user", description = "Registers a new user with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error or username already taken"),
            @ApiResponse(responseCode = "409", description = "User with this email already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody UserRegistrationRequest registrationRequest) {
        try {
            if (registrationRequest.getUsername().length() > 12) {
                return ResponseEntity.badRequest().body(Map.of("error", "Username must not exceed 12 characters."));
            }

            if (userQueryService.existsByUsernameIgnoreCase(registrationRequest.getUsername())) {
                return ResponseEntity.badRequest().body(Map.of("error", "Username is already taken."));
            }

            User user = new User();
            user.setUsername(registrationRequest.getUsername());
            user.setEmail(registrationRequest.getEmail());
            user.setPhone(registrationRequest.getPhone());
            user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            user.setSubscriptionLevel(SubscriptionLevel.LEVEL_1);
            user.setRole("USER");

            userCommandService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "User registered successfully"));
        } catch (InvalidUsernameException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "User with this email already exists."));
        }
    }

    /**
     * Logs in a user and generates a JWT token.
     *
     * @param loginRequest A request containing the email and password of the user to login.
     * @return A ResponseEntity containing the JWT token and a 200 status, or 401 if the login is invalid.
     */
    @Operation(summary = "Login user and generate JWT token", description = "Authenticates user and returns JWT token upon success")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful, JWT token generated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Invalid email or password")
    })
    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody UserLoginRequest loginRequest) {
        Optional<User> user = userQueryService.findByEmail(loginRequest.getEmail());

        if (user.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())) {
            String jwtToken = jwtUtil.generateToken(user.get().getId(), user.get().getRole());
            return ResponseEntity.ok(new UserLoginResponse(jwtToken));
        } else {
            String errorMessage = user.isEmpty() ? "User with this email does not exist." : "Invalid password.";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", errorMessage));
        }
    }


    /**
     * Retrieves all users.
     *
     * @return A list of all users.
     */
    @Operation(summary = "Get all users", description = "Gets all users.")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userQueryService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param userId The unique ID of the user to retrieve.
     * @return A ResponseEntity containing the user and a 200 status, or 404 if the user is not found.
     */
    @Operation(summary = "Get a user by id", description = "Gets a user by his id if it's authorized.")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String userId) {
        return userQueryService.findById(userId)
                .map(UserMapper::toUserResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates a user's subscription level, books history, uploaded books, or saved books.
     *
     * @param userId  The unique ID of the user to update.
     * @param updates A request containing the fields to update.
     * @return A ResponseEntity containing a message and the updated user, or 404 if the user is not found.
     */
    @Operation(summary = "Update user", description = "Updates the subscription level, books history, uploaded books or/and saved books.")
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(
            @PathVariable String userId,
            @RequestBody @Valid UserUpdateRequest updates) {

        User currentUser = authenticationService.getCurrentUser();
        
        boolean isAdmin = "ADMIN".equalsIgnoreCase(currentUser.getRole());
        boolean isSelfUpdate = currentUser.getId().equals(userId);

        if (!isAdmin && !isSelfUpdate) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "You are not allowed to modify this profile."));
        }

        try {
            User updatedUser = userCommandService.updateUserFields(userId, updates);
            return ResponseEntity.ok(
                    Map.of("message", "User updated successfully.", "user", UserMapper.toUserResponse(updatedUser)));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found with ID: " + userId));
        }
    }

    /**
     * Deletes a user from the database.
     *
     * @param userId The unique ID of the user to delete.
     * @return A ResponseEntity with a 204 status if successful, or 404 if the user is not found.
     */
    @Operation(summary = "Delete a user", description = "Deletes a user from the database.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable String userId) {
        try {
            userCommandService.deleteUser(userId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message", "User deleted successfully."));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found with ID: " + userId));
        }
    }
}
