package com.veciapp.api.service;

import java.time.OffsetDateTime;

import org.springframework.stereotype.Service;

import com.veciapp.api.dto.DashboardHomeResponse;
import com.veciapp.api.entity.User;
import com.veciapp.api.repository.AuthorityCenterRepository;
import com.veciapp.api.repository.IncidentReportRepository;
import com.veciapp.api.util.GeoUtils;

@Service
public class DashboardService {

    private final ProfileService profileService;
    private final AuthorityCenterRepository authorityCenterRepository;
    private final IncidentReportRepository incidentReportRepository;

    public DashboardService(
            ProfileService profileService,
            AuthorityCenterRepository authorityCenterRepository,
            IncidentReportRepository incidentReportRepository) {
        this.profileService = profileService;
        this.authorityCenterRepository = authorityCenterRepository;
        this.incidentReportRepository = incidentReportRepository;
    }

    public DashboardHomeResponse getHome(Long userId) {
        User user = profileService.loadUser(userId);
        int nearbyAuthorities;
        if (user.getCurrentLatitude() == null || user.getCurrentLongitude() == null) {
            nearbyAuthorities = authorityCenterRepository.findByActiveTrueOrderByNameAsc().size();
        } else {
            nearbyAuthorities = (int) authorityCenterRepository.findByActiveTrueOrderByNameAsc().stream()
                    .filter(center -> GeoUtils.haversineKm(
                            user.getCurrentLatitude(),
                            user.getCurrentLongitude(),
                            center.getLatitude(),
                            center.getLongitude()) <= 5.0)
                    .count();
        }

        long reportsToday = incidentReportRepository.countByCreatedAtAfter(OffsetDateTime.now().minusDays(1));
        return new DashboardHomeResponse(nearbyAuthorities, reportsToday, user.getSubscriptionPlan());
    }
}

