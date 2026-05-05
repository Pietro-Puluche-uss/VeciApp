package com.veciapp.api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.veciapp.api.dto.CreateIncidentReportRequest;
import com.veciapp.api.dto.IncidentReportResponse;
import com.veciapp.api.entity.IncidentReport;
import com.veciapp.api.entity.User;
import com.veciapp.api.repository.IncidentReportRepository;

@Service
public class IncidentReportService {

    private final IncidentReportRepository incidentReportRepository;
    private final ProfileService profileService;

    public IncidentReportService(IncidentReportRepository incidentReportRepository, ProfileService profileService) {
        this.incidentReportRepository = incidentReportRepository;
        this.profileService = profileService;
    }

    @Transactional
    public IncidentReportResponse create(Long userId, CreateIncidentReportRequest request) {
        User user = profileService.loadUser(userId);
        IncidentReport report = new IncidentReport();
        report.setUser(user);
        report.setCategory(request.category());
        report.setTitle(request.title().trim());
        report.setDescription(request.description().trim());
        report.setAddressReference(blankToNull(request.addressReference()));
        report.setLatitude(request.latitude());
        report.setLongitude(request.longitude());
        report.setEvidenceImageBase64(blankToNull(request.evidenceImageBase64()));
        return toResponse(incidentReportRepository.save(report));
    }

    public List<IncidentReportResponse> listMine(Long userId) {
        return incidentReportRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private IncidentReportResponse toResponse(IncidentReport report) {
        return new IncidentReportResponse(
                report.getId(),
                report.getCategory(),
                report.getCategory().getLabel(),
                report.getStatus(),
                report.getTitle(),
                report.getDescription(),
                report.getAddressReference(),
                report.getLatitude(),
                report.getLongitude(),
                report.getEvidenceImageBase64(),
                report.getCreatedAt());
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
