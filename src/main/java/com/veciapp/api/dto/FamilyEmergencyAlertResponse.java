package com.veciapp.api.dto;

import java.time.OffsetDateTime;

public record FamilyEmergencyAlertResponse(
        Long id,
        Long ownerUserId,
        String ownerFullName,
        Long senderUserId,
        String senderFullName,
        Long emergencyId,
        String groupType,
        Double latitude,
        Double longitude,
        String addressReference,
        OffsetDateTime createdAt,
        OffsetDateTime readAt) {
}
