package com.example.featherbookbackend.usermanagement.interfaces.rest.controller;

import com.example.featherbookbackend.usermanagement.application.commandservices.UserCommandService;
import com.example.featherbookbackend.usermanagement.application.queryservices.UserQueryService;
import com.example.featherbookbackend.usermanagement.domain.model.entities.User;
import com.example.featherbookbackend.usermanagement.infrastructure.security.JwtUtil;
import com.example.featherbookbackend.usermanagement.interfaces.rest.resources.UserLoginRequest;
import com.example.featherbookbackend.usermanagement.interfaces.rest.resources.UserLoginResponse;
import com.example.featherbookbackend.usermanagement.interfaces.rest.resources.UserRegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserCommandService userCommandService;

    @Autowired
    private UserQueryService userQueryService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationRequest registrationRequest) {
        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setEmail(registrationRequest.getEmail());
        user.setPhone(registrationRequest.getPhone());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setSubscriptionLevel(1); // Default subscription level

        userCommandService.createUser(user);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> loginUser(@RequestBody UserLoginRequest loginRequest) {
        Optional<User> user = userQueryService.getAllUsers()
                .stream()
                .filter(u -> u.getEmail().equals(loginRequest.getEmail()) &&
                        passwordEncoder.matches(loginRequest.getPassword(), u.getPassword()))
                .findFirst();

        if (user.isPresent()) {
            String jwtToken = jwtUtil.generateToken(user.get().getUsername());
            return ResponseEntity.ok(new UserLoginResponse(jwtToken));
        } else {
            return ResponseEntity.status(401).body(null);  // Unauthorized
        }
    }
}
