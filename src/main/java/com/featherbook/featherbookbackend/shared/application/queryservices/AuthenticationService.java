package com.featherbook.featherbookbackend.shared.application.queryservices;

import com.featherbook.featherbookbackend.usermanagement.domain.exceptions.UserNotFoundException;
import com.featherbook.featherbookbackend.usermanagement.domain.model.entities.User;
import com.featherbook.featherbookbackend.usermanagement.application.queryservices.UserQueryService;
import com.featherbook.featherbookbackend.usermanagement.infrastructure.security.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * AuthenticationService
 * <p>
 *     Service responsible for handling authentication-related queries.
 * </p>
 */
@Service
public class AuthenticationService {

    private final UserQueryService userQueryService;
    private final JwtUtil jwtUtil;

    /**
     * Constructor
     * @param userQueryService
     * @param jwtUtil
     */
    public AuthenticationService(UserQueryService userQueryService, JwtUtil jwtUtil) {
        this.userQueryService = userQueryService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Get the current authenticated user
     * @return User
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new UserNotFoundException("No authenticated user found");
        }

        String token = (String) authentication.getCredentials();
        String userId = jwtUtil.extractUserId(token);

        return userQueryService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Authenticated user not found"));
    }
}
