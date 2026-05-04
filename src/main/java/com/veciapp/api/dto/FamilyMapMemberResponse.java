package com.veciapp.api.dto;

public record FamilyMapMemberResponse(
        Long userId,
        String fullName,
        String alias,
        String relationshipLabel,
        Double latitude,
        Double longitude,
        String district,
        String city,
        String profilePhotoUrl) {
}

