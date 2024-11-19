package com.featherbook.featherbookbackend.usermanagement.infrastructure.security;

import com.featherbook.featherbookbackend.usermanagement.domain.model.entities.User;
import com.featherbook.featherbookbackend.usermanagement.infrastructure.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

        private final UserRepository userRepository;

        public CustomUserDetailsService(UserRepository userRepository) {
                this.userRepository = userRepository;
        }

        @Override
        public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));
                return new CustomUserDetails(user);
        }
}
