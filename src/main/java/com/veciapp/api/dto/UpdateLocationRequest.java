package com.veciapp.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateLocationRequest(
        @NotNull Double latitude,
        @NotNull Double longitude,
        @Size(max = 120) String district,
        @Size(max = 120) String city) {
}

