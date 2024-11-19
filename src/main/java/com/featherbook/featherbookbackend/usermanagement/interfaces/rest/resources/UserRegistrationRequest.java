package com.featherbook.featherbookbackend.usermanagement.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request payload for registering a new user")
public class UserRegistrationRequest {

    @NotBlank(message = "Username is required")
    @Size(max = 12, message = "Username must not exceed 12 characters")
    @Pattern(regexp = "^[^\\s]+$", message = "Invalid username, should not contain spaces")
    @Schema(description = "Username of the new user", example = "JaneDoe")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Schema(description = "Email of the new user", example = "ejemplo@gmail.com")
    private String email;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "\\d{9}", message = "Phone must be a 9-digit number")
    @Schema(description = "Phone number of the new user", example = "999888777")
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must have at least 8 characters")
    @Schema(description = "Password of the new user", example = "MiContraseña")
    private String password;
}
