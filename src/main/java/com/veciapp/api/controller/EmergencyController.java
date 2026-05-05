package com.veciapp.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.veciapp.api.dto.CreateEmergencyRequest;
import com.veciapp.api.dto.EmergencyResponse;
import com.veciapp.api.security.SecurityUtils;
import com.veciapp.api.service.EmergencyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/emergencies")
public class EmergencyController {

    private final EmergencyService emergencyService;

    public EmergencyController(EmergencyService emergencyService) {
        this.emergencyService = emergencyService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmergencyResponse create(
            Authentication authentication,
            @Valid @RequestBody CreateEmergencyRequest request) {
        return emergencyService.create(SecurityUtils.currentUserId(authentication), request);
    }

    @GetMapping("/mine")
    public List<EmergencyResponse> listMine(Authentication authentication) {
        return emergencyService.listMine(SecurityUtils.currentUserId(authentication));
    }

    @GetMapping("/mine/{id}")
    public EmergencyResponse getMineById(
            Authentication authentication,
            @PathVariable Long id) {
        return emergencyService.getMineById(SecurityUtils.currentUserId(authentication), id);
    }
}
