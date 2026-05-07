package com.veciapp.api.service;

import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.veciapp.api.dto.FamilyEmergencyAlertResponse;
import com.veciapp.api.entity.EmergencyAlert;
import com.veciapp.api.entity.FamilyEmergencyNotification;
import com.veciapp.api.entity.FamilyMember;
import com.veciapp.api.entity.User;
import com.veciapp.api.exception.ForbiddenException;
import com.veciapp.api.exception.ResourceNotFoundException;
import com.veciapp.api.model.FamilyGroupType;
import com.veciapp.api.model.FamilyMemberStatus;
import com.veciapp.api.model.SubscriptionPlan;
import com.veciapp.api.repository.FamilyEmergencyNotificationRepository;
import com.veciapp.api.repository.FamilyMemberRepository;

@Service
public class FamilyEmergencyNotificationService {

    private static final int ALERT_RETENTION_DAYS = 7;

    private final FamilyEmergencyNotificationRepository notificationRepository;
    private final FamilyMemberRepository familyMemberRepository;
    private final ProfileService profileService;

    public FamilyEmergencyNotificationService(
            FamilyEmergencyNotificationRepository notificationRepository,
            FamilyMemberRepository familyMemberRepository,
            ProfileService profileService) {
        this.notificationRepository = notificationRepository;
        this.familyMemberRepository = familyMemberRepository;
        this.profileService = profileService;
    }

    @Transactional
    public void notifyEmergencyToGroups(Long senderUserId, EmergencyAlert alert) {
        User sender = profileService.loadUser(senderUserId);
        Set<Long> ownerIds = new LinkedHashSet<>();
        if (sender.getSubscriptionPlan() == SubscriptionPlan.FAMILY) {
            ownerIds.add(sender.getId());
        }
        familyMemberRepository.findByMemberIdOrderByCreatedAtAsc(senderUserId).stream()
                .filter(this::isAccepted)
                .forEach(member -> ownerIds.add(member.getOwner().getId()));

        for (Long ownerId : ownerIds) {
            User owner = profileService.loadUser(ownerId);
            List<FamilyMember> acceptedMembers = familyMemberRepository.findByOwnerIdOrderByCreatedAtAsc(ownerId)
                    .stream()
                    .filter(this::isAccepted)
                    .toList();
            FamilyGroupType senderGroupType = resolveSenderGroupType(ownerId, senderUserId, acceptedMembers);
            createNotificationForGroup(owner, sender, alert, senderGroupType, acceptedMembers);
        }
    }

    @Transactional
    public List<FamilyEmergencyAlertResponse> listMyAlerts(Long userId) {
        cleanupExpiredAlerts(userId);
        OffsetDateTime cutoff = OffsetDateTime.now().minusDays(ALERT_RETENTION_DAYS);
        return notificationRepository.findByRecipientIdAndCreatedAtAfterOrderByCreatedAtDesc(userId, cutoff).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void markAsRead(Long userId, Long notificationId) {
        FamilyEmergencyNotification notification = notificationRepository.findByIdAndRecipientId(notificationId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Notificacion no encontrada"));
        if (notification.getReadAt() == null) {
            notification.setReadAt(OffsetDateTime.now());
            notificationRepository.save(notification);
        }
    }

    @Transactional
    public void deleteAlert(Long userId, Long notificationId) {
        long deleted = notificationRepository.deleteByIdAndRecipientId(notificationId, userId);
        if (deleted == 0) {
            throw new ResourceNotFoundException("Notificacion no encontrada");
        }
    }

    @Transactional
    public void clearAllAlerts(Long userId) {
        notificationRepository.deleteByRecipientId(userId);
    }

    private void createNotificationForGroup(
            User owner,
            User sender,
            EmergencyAlert alert,
            FamilyGroupType senderGroupType,
            List<FamilyMember> acceptedMembers) {
        createNotification(owner, owner, sender, alert, senderGroupType);
        acceptedMembers.forEach(member -> createNotification(owner, member.getMember(), sender, alert, member.getGroupType()));
    }

    private void createNotification(
            User owner,
            User recipient,
            User sender,
            EmergencyAlert alert,
            FamilyGroupType groupType) {
        if (recipient.getId().equals(sender.getId())) {
            return;
        }
        FamilyEmergencyNotification notification = new FamilyEmergencyNotification();
        notification.setOwner(owner);
        notification.setRecipient(recipient);
        notification.setSender(sender);
        notification.setEmergencyAlert(alert);
        notification.setGroupType(groupType == null ? FamilyGroupType.FAMILY : groupType);
        notificationRepository.save(notification);
    }

    private FamilyGroupType resolveSenderGroupType(Long ownerId, Long senderUserId, List<FamilyMember> acceptedMembers) {
        if (ownerId.equals(senderUserId)) {
            return FamilyGroupType.FAMILY;
        }
        return acceptedMembers.stream()
                .filter(member -> member.getMember().getId().equals(senderUserId))
                .map(FamilyMember::getGroupType)
                .findFirst()
                .orElse(FamilyGroupType.FAMILY);
    }

    private FamilyEmergencyAlertResponse toResponse(FamilyEmergencyNotification notification) {
        EmergencyAlert alert = notification.getEmergencyAlert();
        return new FamilyEmergencyAlertResponse(
                notification.getId(),
                notification.getOwner().getId(),
                notification.getOwner().getFullName(),
                notification.getSender().getId(),
                notification.getSender().getFullName(),
                alert.getId(),
                notification.getGroupType().name(),
                alert.getLatitude(),
                alert.getLongitude(),
                alert.getAddressReference(),
                notification.getCreatedAt(),
                notification.getReadAt());
    }

    private void cleanupExpiredAlerts(Long userId) {
        OffsetDateTime cutoff = OffsetDateTime.now().minusDays(ALERT_RETENTION_DAYS);
        notificationRepository.deleteByRecipientIdAndCreatedAtBefore(userId, cutoff);
    }

    private boolean isAccepted(FamilyMember familyMember) {
        return familyMember.getStatus() == FamilyMemberStatus.ACCEPTED;
    }
}
