package com.veciapp.api.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.veciapp.api.model.EmergencyStatus;
import com.veciapp.api.model.EmergencyType;

public record EmergencyResponse(
        Long id,
        EmergencyType type,
        String typeLabel,
        EmergencyStatus status,
        Double latitude,
        Double longitude,
        String addressReference,
        String notes,
        String assignedAuthorityName,
        BigDecimal assignedDistanceKm,
        Integer estimatedResponseMinutes,
        OffsetDateTime createdAt) {
}

