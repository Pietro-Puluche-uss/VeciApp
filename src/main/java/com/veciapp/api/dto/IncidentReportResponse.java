package com.veciapp.api.dto;

import java.time.OffsetDateTime;

import com.veciapp.api.model.ReportCategory;
import com.veciapp.api.model.ReportStatus;

public record IncidentReportResponse(
        Long id,
        ReportCategory category,
        String categoryLabel,
        ReportStatus status,
        String title,
        String description,
        String addressReference,
        Double latitude,
        Double longitude,
        String evidenceImageBase64,
        OffsetDateTime createdAt) {
}
