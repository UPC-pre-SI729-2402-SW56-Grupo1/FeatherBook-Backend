package com.example.featherbookbackend.usermanagement.interfaces.rest.resources;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserLoginResponse {
    private String jwtToken;
}
