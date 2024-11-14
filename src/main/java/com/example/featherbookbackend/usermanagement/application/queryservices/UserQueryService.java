package com.example.featherbookbackend.usermanagement.application.queryservices;

import com.example.featherbookbackend.usermanagement.domain.model.entities.User;
import com.example.featherbookbackend.usermanagement.infrastructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserQueryService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUserById(String userId) {
        return userRepository.findById(userId);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
