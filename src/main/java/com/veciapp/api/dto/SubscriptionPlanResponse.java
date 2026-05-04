package com.veciapp.api.dto;

import java.math.BigDecimal;
import java.util.List;

import com.veciapp.api.model.SubscriptionPlan;

public record SubscriptionPlanResponse(
        SubscriptionPlan code,
        String name,
        BigDecimal monthlyPrice,
        List<String> features) {
}

