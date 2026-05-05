package com.veciapp.api.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.veciapp.api.model.EmergencyStatus;
import com.veciapp.api.model.EmergencyType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "emergency_alert")
public class EmergencyAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EmergencyType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EmergencyStatus status;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(length = 180)
    private String addressReference;

    @Column(length = 300)
    private String notes;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String evidenceImageBase64;

    @Column(length = 120)
    private String assignedAuthorityName;

    @Column(precision = 8, scale = 2)
    private BigDecimal assignedDistanceKm;

    private Integer estimatedResponseMinutes;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
        if (status == null) {
            status = EmergencyStatus.RECEIVED;
        }
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public EmergencyType getType() {
        return type;
    }

    public void setType(EmergencyType type) {
        this.type = type;
    }

    public EmergencyStatus getStatus() {
        return status;
    }

    public void setStatus(EmergencyStatus status) {
        this.status = status;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddressReference() {
        return addressReference;
    }

    public void setAddressReference(String addressReference) {
        this.addressReference = addressReference;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getEvidenceImageBase64() {
        return evidenceImageBase64;
    }

    public void setEvidenceImageBase64(String evidenceImageBase64) {
        this.evidenceImageBase64 = evidenceImageBase64;
    }

    public String getAssignedAuthorityName() {
        return assignedAuthorityName;
    }

    public void setAssignedAuthorityName(String assignedAuthorityName) {
        this.assignedAuthorityName = assignedAuthorityName;
    }

    public BigDecimal getAssignedDistanceKm() {
        return assignedDistanceKm;
    }

    public void setAssignedDistanceKm(BigDecimal assignedDistanceKm) {
        this.assignedDistanceKm = assignedDistanceKm;
    }

    public Integer getEstimatedResponseMinutes() {
        return estimatedResponseMinutes;
    }

    public void setEstimatedResponseMinutes(Integer estimatedResponseMinutes) {
        this.estimatedResponseMinutes = estimatedResponseMinutes;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
