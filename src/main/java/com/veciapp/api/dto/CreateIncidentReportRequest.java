package com.veciapp.api.dto;

import com.veciapp.api.model.ReportCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateIncidentReportRequest(
        @NotNull ReportCategory category,
        @NotBlank @Size(max = 140) String title,
        @NotBlank @Size(max = 600) String description,
        @Size(max = 180) String addressReference,
        Double latitude,
        Double longitude) {
}

