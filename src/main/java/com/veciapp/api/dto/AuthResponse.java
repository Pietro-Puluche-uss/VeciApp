package com.veciapp.api.dto;

import com.veciapp.api.model.SubscriptionPlan;

public record AuthResponse(
        Long userId,
        String fullName,
        String token,
        SubscriptionPlan subscriptionPlan) {
}

