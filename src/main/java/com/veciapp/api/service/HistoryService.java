package com.veciapp.api.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.veciapp.api.dto.HistoryItemResponse;
import com.veciapp.api.repository.EmergencyAlertRepository;
import com.veciapp.api.repository.IncidentReportRepository;

@Service
public class HistoryService {

    private final EmergencyAlertRepository emergencyAlertRepository;
    private final IncidentReportRepository incidentReportRepository;

    public HistoryService(
            EmergencyAlertRepository emergencyAlertRepository,
            IncidentReportRepository incidentReportRepository) {
        this.emergencyAlertRepository = emergencyAlertRepository;
        this.incidentReportRepository = incidentReportRepository;
    }

    public List<HistoryItemResponse> listMine(Long userId) {
        List<HistoryItemResponse> items = new ArrayList<>();

        emergencyAlertRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .forEach(alert -> items.add(new HistoryItemResponse(
                        "emergency",
                        alert.getId(),
                        alert.getType().getLabel(),
                        alert.getAssignedAuthorityName() != null
                                ? "Asignada a " + alert.getAssignedAuthorityName()
                                : "Alerta recibida",
                        alert.getStatus().name(),
                        alert.getAddressReference(),
                        alert.getCreatedAt())));

        incidentReportRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .forEach(report -> items.add(new HistoryItemResponse(
                        "report",
                        report.getId(),
                        report.getTitle(),
                        report.getCategory().getLabel(),
                        report.getStatus().name(),
                        report.getAddressReference(),
                        report.getCreatedAt())));

        return items.stream()
                .sorted(Comparator.comparing(HistoryItemResponse::createdAt).reversed())
                .toList();
    }
}

