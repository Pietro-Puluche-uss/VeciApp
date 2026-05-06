package com.veciapp.api.service;

import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.veciapp.api.dto.ProfileResponse;
import com.veciapp.api.dto.UpdateLocationRequest;
import com.veciapp.api.dto.UpdateProfileRequest;
import com.veciapp.api.entity.User;
import com.veciapp.api.exception.BadRequestException;
import com.veciapp.api.exception.ResourceNotFoundException;
import com.veciapp.api.repository.UserRepository;

@Service
public class ProfileService {

    private final UserRepository userRepository;

    public ProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ProfileResponse getProfile(Long userId) {
        User user = loadUser(userId);
        return toResponse(user);
    }

    @Transactional
    public ProfileResponse updateProfile(Long userId, UpdateProfileRequest request) {
        User user = loadUser(userId);
        String newEmail = request.email().trim().toLowerCase(Locale.ROOT);
        String newPhone = request.phone().trim();
        userRepository.findByEmailIgnoreCase(newEmail)
                .filter(other -> !other.getId().equals(userId))
                .ifPresent(other -> {
                    throw new BadRequestException("El correo ya esta registrado");
                });
        userRepository.findByPhone(newPhone)
                .filter(other -> !other.getId().equals(userId))
                .ifPresent(other -> {
                    throw new BadRequestException("El telefono ya esta registrado");
                });
        user.setEmail(newEmail);
        user.setPhone(newPhone);
        user.setProfilePhotoUrl(blankToNull(request.profilePhotoUrl()));
        return toResponse(userRepository.save(user));
    }

    @Transactional
    public ProfileResponse updateLocation(Long userId, UpdateLocationRequest request) {
        User user = loadUser(userId);
        user.setCurrentLatitude(request.latitude());
        user.setCurrentLongitude(request.longitude());
        user.setDistrict(blankToNull(request.district()));
        user.setCity(blankToNull(request.city()));
        return toResponse(userRepository.save(user));
    }

    public User loadUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    public ProfileResponse toResponse(User user) {
        return new ProfileResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getDocumentNumber(),
                user.getProfilePhotoUrl(),
                user.getCurrentLatitude(),
                user.getCurrentLongitude(),
                user.getDistrict(),
                user.getCity(),
                user.getSubscriptionPlan(),
                user.getSubscriptionStatus(),
                user.getSubscriptionActivatedAt(),
                user.getCreatedAt());
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
