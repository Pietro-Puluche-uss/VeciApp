package com.veciapp.api.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.veciapp.api.dto.CreateEmergencyRequest;
import com.veciapp.api.dto.EmergencyResponse;
import com.veciapp.api.entity.AuthorityCenter;
import com.veciapp.api.entity.EmergencyAlert;
import com.veciapp.api.entity.User;
import com.veciapp.api.exception.BadRequestException;
import com.veciapp.api.exception.ResourceNotFoundException;
import com.veciapp.api.repository.AuthorityCenterRepository;
import com.veciapp.api.repository.EmergencyAlertRepository;
import com.veciapp.api.util.GeoUtils;

@Service
public class EmergencyService {

    private final EmergencyAlertRepository emergencyAlertRepository;
    private final AuthorityCenterRepository authorityCenterRepository;
    private final ProfileService profileService;

    public EmergencyService(
            EmergencyAlertRepository emergencyAlertRepository,
            AuthorityCenterRepository authorityCenterRepository,
            ProfileService profileService) {
        this.emergencyAlertRepository = emergencyAlertRepository;
        this.authorityCenterRepository = authorityCenterRepository;
        this.profileService = profileService;
    }

    @Transactional
    public EmergencyResponse create(Long userId, CreateEmergencyRequest request) {
        User user = profileService.loadUser(userId);
        Double latitude = request.latitude() != null ? request.latitude() : user.getCurrentLatitude();
        Double longitude = request.longitude() != null ? request.longitude() : user.getCurrentLongitude();
        if (latitude == null || longitude == null) {
            throw new BadRequestException("Debes enviar ubicacion para registrar la emergencia");
        }

        EmergencyAlert alert = new EmergencyAlert();
        alert.setUser(user);
        alert.setType(request.type());
        alert.setLatitude(latitude);
        alert.setLongitude(longitude);
        alert.setAddressReference(blankToNull(request.addressReference()));
        alert.setNotes(blankToNull(request.notes()));
        alert.setEvidenceImageBase64(blankToNull(request.evidenceImageBase64()));

        authorityCenterRepository.findByActiveTrueOrderByNameAsc().stream()
                .min(Comparator.comparingDouble(center -> GeoUtils.haversineKm(
                        latitude, longitude, center.getLatitude(), center.getLongitude())))
                .ifPresent(center -> assignNearestCenter(alert, latitude, longitude, center));

        return toResponse(emergencyAlertRepository.save(alert));
    }

    public List<EmergencyResponse> listMine(Long userId) {
        return emergencyAlertRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public EmergencyResponse getMineById(Long userId, Long alertId) {
        EmergencyAlert alert = emergencyAlertRepository.findByIdAndUserId(alertId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Emergencia no encontrada"));
        return toDetailResponse(alert);
    }

    private void assignNearestCenter(EmergencyAlert alert, Double latitude, Double longitude, AuthorityCenter center) {
        double distance = GeoUtils.haversineKm(latitude, longitude, center.getLatitude(), center.getLongitude());
        int etaMinutes = Math.max(3, (int) Math.ceil(distance * 4));
        alert.setAssignedAuthorityName(center.getName());
        alert.setAssignedDistanceKm(BigDecimal.valueOf(distance).setScale(2, RoundingMode.HALF_UP));
        alert.setEstimatedResponseMinutes(etaMinutes);
    }

    private EmergencyResponse toResponse(EmergencyAlert alert) {
        return toResponse(alert, false);
    }

    private EmergencyResponse toDetailResponse(EmergencyAlert alert) {
        return toResponse(alert, true);
    }

    private EmergencyResponse toResponse(EmergencyAlert alert, boolean includeEvidenceImage) {
        return new EmergencyResponse(
                alert.getId(),
                alert.getType(),
                alert.getType().getLabel(),
                alert.getStatus(),
                alert.getLatitude(),
                alert.getLongitude(),
                alert.getAddressReference(),
                alert.getNotes(),
                includeEvidenceImage ? alert.getEvidenceImageBase64() : null,
                alert.getAssignedAuthorityName(),
                alert.getAssignedDistanceKm(),
                alert.getEstimatedResponseMinutes(),
                alert.getCreatedAt());
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
