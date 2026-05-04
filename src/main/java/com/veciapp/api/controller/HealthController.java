package com.veciapp.api.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/")
    public Map<String, Object> root() {
        return Map.of(
                "service", "VeciApp API",
                "status", "UP");
    }

    @GetMapping("/api/health")
    public Map<String, Object> apiHealth() {
        return Map.of(
                "service", "VeciApp API",
                "status", "UP");
    }
}

