package com.veciapp.api.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.veciapp.api.dto.ProfileResponse;
import com.veciapp.api.dto.UpdateLocationRequest;
import com.veciapp.api.dto.UpdateProfileRequest;
import com.veciapp.api.security.SecurityUtils;
import com.veciapp.api.service.ProfileService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/me")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/profile")
    public ProfileResponse getProfile(Authentication authentication) {
        return profileService.getProfile(SecurityUtils.currentUserId(authentication));
    }

    @PutMapping("/profile")
    public ProfileResponse updateProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequest request) {
        return profileService.updateProfile(SecurityUtils.currentUserId(authentication), request);
    }

    @PutMapping("/location")
    public ProfileResponse updateLocation(
            Authentication authentication,
            @Valid @RequestBody UpdateLocationRequest request) {
        return profileService.updateLocation(SecurityUtils.currentUserId(authentication), request);
    }
}

