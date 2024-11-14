package com.example.featherbookbackend.usermanagement.application.commandservices;

import com.example.featherbookbackend.usermanagement.domain.model.entities.User;
import com.example.featherbookbackend.usermanagement.infrastructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserCommandService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(String userId, User updatedUser) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            updatedUser.setId(userId);
            return userRepository.save(updatedUser);
        }
        return null;  // Implement error handling as needed
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}
