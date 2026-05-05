package com.veciapp.api.repository;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.veciapp.api.entity.IncidentReport;

public interface IncidentReportRepository extends JpaRepository<IncidentReport, Long> {

    List<IncidentReport> findByUserIdOrderByCreatedAtDesc(Long userId);

    java.util.Optional<IncidentReport> findByIdAndUserId(Long id, Long userId);

    long countByCreatedAtAfter(OffsetDateTime createdAt);
}
