package com.veciapp.api.model;

public enum EmergencyType {
    ROBBERY("Robo/Asalto"),
    VIOLENCE("Violencia"),
    ACCIDENT("Accidente"),
    MISSING_PERSON("Persona desaparecida"),
    THREAT("Amenaza"),
    OTHER("Otro");

    private final String label;

    EmergencyType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

