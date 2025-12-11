package com.example.realestatehub.service.impl;

import com.example.realestatehub.common.exception.BusinessException;
import com.example.realestatehub.dto.auth.AuthResponse;
import com.example.realestatehub.dto.auth.LoginRequest;
import com.example.realestatehub.dto.auth.RegisterRequest;
import com.example.realestatehub.model.entity.User;
import com.example.realestatehub.model.enums.Role;
import com.example.realestatehub.repository.UserRepository;
import com.example.realestatehub.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements com.example.realestatehub.service.AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new BusinessException("手机号已注册");
        }
        User user = new User();
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setRole(Role.USER);
        userRepository.save(user);
        String token = tokenProvider.generateToken(user.getPhone());
        return new AuthResponse(token, user.getRole(), user.getName());
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(request.getPhone(), request.getPassword());
        authenticationManager.authenticate(token);
        User user = userRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new BusinessException("用户不存在"));
        String jwt = tokenProvider.generateToken(user.getPhone());
        return new AuthResponse(jwt, user.getRole(), user.getName());
    }
}

