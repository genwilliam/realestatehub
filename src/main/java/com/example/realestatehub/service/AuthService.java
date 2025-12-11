package com.example.realestatehub.service;

import com.example.realestatehub.dto.auth.AuthResponse;
import com.example.realestatehub.dto.auth.LoginRequest;
import com.example.realestatehub.dto.auth.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}

