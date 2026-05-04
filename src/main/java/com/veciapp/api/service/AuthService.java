package com.veciapp.api.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.veciapp.api.dto.AuthResponse;
import com.veciapp.api.dto.LoginRequest;
import com.veciapp.api.dto.RegisterRequest;
import com.veciapp.api.entity.User;
import com.veciapp.api.exception.BadRequestException;
import com.veciapp.api.exception.ForbiddenException;
import com.veciapp.api.exception.UnauthorizedException;
import com.veciapp.api.repository.UserRepository;
import com.veciapp.api.security.JwtService;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();
        String phone = request.phone().trim();

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new BadRequestException("El correo ya esta registrado");
        }
        if (userRepository.existsByPhone(phone)) {
            throw new BadRequestException("El telefono ya esta registrado");
        }

        User user = new User();
        user.setFirstName(request.firstName().trim());
        user.setLastName(request.lastName().trim());
        user.setEmail(email);
        user.setPhone(phone);
        user.setDocumentNumber(blankToNull(request.documentNumber()));
        user.setPasswordHash(passwordEncoder.encode(request.password()));

        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(savedUser.getId(), savedUser.getEmail());
        return new AuthResponse(savedUser.getId(), savedUser.getFullName(), token, savedUser.getSubscriptionPlan());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.email().trim())
                .orElseThrow(() -> new UnauthorizedException("Credenciales invalidas"));
        if (!user.isActive()) {
            throw new ForbiddenException("La cuenta esta inactiva");
        }
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new UnauthorizedException("Credenciales invalidas");
        }
        String token = jwtService.generateToken(user.getId(), user.getEmail());
        return new AuthResponse(user.getId(), user.getFullName(), token, user.getSubscriptionPlan());
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}

