package com.veciapp.api.repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.veciapp.api.entity.FamilyEmergencyNotification;

public interface FamilyEmergencyNotificationRepository extends JpaRepository<FamilyEmergencyNotification, Long> {

    List<FamilyEmergencyNotification> findByRecipientIdOrderByCreatedAtDesc(Long recipientId);

    List<FamilyEmergencyNotification> findByRecipientIdAndCreatedAtAfterOrderByCreatedAtDesc(
            Long recipientId,
            OffsetDateTime createdAt);

    Optional<FamilyEmergencyNotification> findByIdAndRecipientId(Long id, Long recipientId);

    long deleteByIdAndRecipientId(Long id, Long recipientId);

    long deleteByRecipientId(Long recipientId);

    long deleteByRecipientIdAndCreatedAtBefore(Long recipientId, OffsetDateTime createdAt);
}
