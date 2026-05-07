package com.veciapp.api.dto;

import java.time.OffsetDateTime;

public record FamilyInvitationResponse(
        Long id,
        Long ownerUserId,
        String ownerFullName,
        String ownerEmail,
        String alias,
        String relationshipLabel,
        String groupType,
        OffsetDateTime createdAt) {
}
