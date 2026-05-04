package com.veciapp.api.dto;

import com.veciapp.api.model.SubscriptionPlan;

import jakarta.validation.constraints.NotNull;

public record UpdateSubscriptionRequest(@NotNull SubscriptionPlan plan) {
}

