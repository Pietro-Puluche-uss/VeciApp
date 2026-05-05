package com.veciapp.api.dto;

import com.veciapp.api.model.EmergencyType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateEmergencyRequest(
        @NotNull EmergencyType type,
        Double latitude,
        Double longitude,
        @Size(max = 180) String addressReference,
        @Size(max = 300) String notes,
        @Size(max = 2_000_000) String evidenceImageBase64) {
}
