package com.veciapp.api.dto;

import java.time.OffsetDateTime;

import com.veciapp.api.model.SubscriptionPlan;
import com.veciapp.api.model.SubscriptionStatus;

public record UserSubscriptionResponse(
        SubscriptionPlan currentPlan,
        SubscriptionStatus status,
        OffsetDateTime activatedAt) {
}

