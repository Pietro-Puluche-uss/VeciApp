package com.veciapp.api.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.veciapp.api.dto.SubscriptionPlanResponse;
import com.veciapp.api.dto.UpdateSubscriptionRequest;
import com.veciapp.api.dto.UserSubscriptionResponse;
import com.veciapp.api.security.SecurityUtils;
import com.veciapp.api.service.SubscriptionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/plans")
    public List<SubscriptionPlanResponse> listPlans() {
        return subscriptionService.listPlans();
    }

    @GetMapping("/me")
    public UserSubscriptionResponse getCurrent(Authentication authentication) {
        return subscriptionService.getCurrent(SecurityUtils.currentUserId(authentication));
    }

    @PutMapping("/me")
    public UserSubscriptionResponse update(
            Authentication authentication,
            @Valid @RequestBody UpdateSubscriptionRequest request) {
        return subscriptionService.update(SecurityUtils.currentUserId(authentication), request);
    }
}

