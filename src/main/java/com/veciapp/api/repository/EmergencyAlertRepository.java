package com.veciapp.api.repository;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.veciapp.api.entity.EmergencyAlert;

public interface EmergencyAlertRepository extends JpaRepository<EmergencyAlert, Long> {

    List<EmergencyAlert> findByUserIdOrderByCreatedAtDesc(Long userId);

    long countByCreatedAtAfter(OffsetDateTime createdAt);
}

