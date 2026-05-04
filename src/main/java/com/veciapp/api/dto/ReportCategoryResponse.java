package com.veciapp.api.dto;

import com.veciapp.api.model.ReportCategory;

public record ReportCategoryResponse(
        String id,
        String label) {

    public static ReportCategoryResponse from(ReportCategory category) {
        return new ReportCategoryResponse(category.name(), category.getLabel());
    }
}

