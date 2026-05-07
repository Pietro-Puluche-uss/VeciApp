package com.veciapp.api.model;

public enum FamilyGroupType {
    FAMILY,
    OTHER;

    public static FamilyGroupType fromValue(String value) {
        if (value == null || value.isBlank()) {
            return FAMILY;
        }
        return switch (value.trim().toUpperCase()) {
            case "OTHER" -> OTHER;
            default -> FAMILY;
        };
    }
}
