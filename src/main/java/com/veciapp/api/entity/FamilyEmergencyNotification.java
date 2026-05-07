package com.veciapp.api.entity;

import java.time.OffsetDateTime;

import com.veciapp.api.model.FamilyGroupType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "family_emergency_notification")
public class FamilyEmergencyNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipient_id")
    private User recipient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emergency_alert_id")
    private EmergencyAlert emergencyAlert;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private FamilyGroupType groupType;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    private OffsetDateTime readAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
        if (groupType == null) {
            groupType = FamilyGroupType.FAMILY;
        }
    }

    public Long getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public EmergencyAlert getEmergencyAlert() {
        return emergencyAlert;
    }

    public void setEmergencyAlert(EmergencyAlert emergencyAlert) {
        this.emergencyAlert = emergencyAlert;
    }

    public FamilyGroupType getGroupType() {
        return groupType;
    }

    public void setGroupType(FamilyGroupType groupType) {
        this.groupType = groupType;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(OffsetDateTime readAt) {
        this.readAt = readAt;
    }
}
