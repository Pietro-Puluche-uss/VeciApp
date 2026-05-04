package com.veciapp.api.dto;

import java.time.OffsetDateTime;

public record FamilyMemberResponse(
        Long id,
        Long memberUserId,
        String fullName,
        String email,
        String phone,
        String alias,
        String relationshipLabel,
        OffsetDateTime createdAt) {
}

