package com.veciapp.api.dto;

import com.veciapp.api.model.SubscriptionPlan;

public record DashboardHomeResponse(
        int nearbyAuthorities,
        long reportsToday,
        SubscriptionPlan subscriptionPlan) {
}

