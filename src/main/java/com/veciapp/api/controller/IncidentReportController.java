package com.veciapp.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.veciapp.api.dto.CreateIncidentReportRequest;
import com.veciapp.api.dto.IncidentReportResponse;
import com.veciapp.api.security.SecurityUtils;
import com.veciapp.api.service.IncidentReportService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reports")
public class IncidentReportController {

    private final IncidentReportService incidentReportService;

    public IncidentReportController(IncidentReportService incidentReportService) {
        this.incidentReportService = incidentReportService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IncidentReportResponse create(
            Authentication authentication,
            @Valid @RequestBody CreateIncidentReportRequest request) {
        return incidentReportService.create(SecurityUtils.currentUserId(authentication), request);
    }

    @GetMapping("/mine")
    public List<IncidentReportResponse> listMine(Authentication authentication) {
        return incidentReportService.listMine(SecurityUtils.currentUserId(authentication));
    }
}

