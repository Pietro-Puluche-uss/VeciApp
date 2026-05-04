package com.veciapp.api.dto;

import java.time.OffsetDateTime;

import com.veciapp.api.model.SubscriptionPlan;
import com.veciapp.api.model.SubscriptionStatus;

public record ProfileResponse(
        Long userId,
        String firstName,
        String lastName,
        String fullName,
        String email,
        String phone,
        String documentNumber,
        String profilePhotoUrl,
        Double currentLatitude,
        Double currentLongitude,
        String district,
        String city,
        SubscriptionPlan subscriptionPlan,
        SubscriptionStatus subscriptionStatus,
        OffsetDateTime subscriptionActivatedAt,
        OffsetDateTime createdAt) {
}

