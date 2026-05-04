package com.veciapp.api.dto;

import java.time.OffsetDateTime;

public record HistoryItemResponse(
        String itemType,
        Long itemId,
        String title,
        String subtitle,
        String status,
        String location,
        OffsetDateTime createdAt) {
}

