package com.veciapp.api.model;

public enum ReportCategory {
    TRAFFIC_ACCIDENT("Accidente de transito"),
    DAMAGED_ROAD("Via danada"),
    POOR_LIGHTING("Falta de iluminacion"),
    GARBAGE_ACCUMULATION("Acumulacion de basura"),
    SUSPICIOUS_BEHAVIOR("Comportamiento sospechoso"),
    OTHER("Otro");

    private final String label;

    ReportCategory(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

