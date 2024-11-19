package com.featherbook.featherbookbackend.usermanagement.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
