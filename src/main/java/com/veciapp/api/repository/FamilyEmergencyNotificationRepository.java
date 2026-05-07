package com.veciapp.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.veciapp.api.entity.FamilyEmergencyNotification;

public interface FamilyEmergencyNotificationRepository extends JpaRepository<FamilyEmergencyNotification, Long> {

    List<FamilyEmergencyNotification> findByRecipientIdOrderByCreatedAtDesc(Long recipientId);

    Optional<FamilyEmergencyNotification> findByIdAndRecipientId(Long id, Long recipientId);
}
