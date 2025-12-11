package com.example.realestatehub.dto.auth;

import com.example.realestatehub.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Role role;
    private String name;
}

