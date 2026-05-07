package com.veciapp.api.entity;

import java.time.OffsetDateTime;

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
import jakarta.persistence.UniqueConstraint;

import com.veciapp.api.model.FamilyGroupType;
import com.veciapp.api.model.FamilyMemberStatus;

@Entity
@Table(
        name = "family_member",
        uniqueConstraints = @UniqueConstraint(name = "uk_owner_member", columnNames = {"owner_id", "member_id"}))
public class FamilyMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    private User member;

    @Column(length = 80)
    private String alias;

    @Column(length = 60)
    private String relationshipLabel;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private FamilyGroupType groupType;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private FamilyMemberStatus status;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    void prePersist() {
        if (groupType == null) {
            groupType = FamilyGroupType.FAMILY;
        }
        if (status == null) {
            status = FamilyMemberStatus.ACCEPTED;
        }
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
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

    public User getMember() {
        return member;
    }

    public void setMember(User member) {
        this.member = member;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getRelationshipLabel() {
        return relationshipLabel;
    }

    public void setRelationshipLabel(String relationshipLabel) {
        this.relationshipLabel = relationshipLabel;
    }

    public FamilyGroupType getGroupType() {
        return groupType == null ? FamilyGroupType.FAMILY : groupType;
    }

    public void setGroupType(FamilyGroupType groupType) {
        this.groupType = groupType;
    }

    public FamilyMemberStatus getStatus() {
        return status == null ? FamilyMemberStatus.ACCEPTED : status;
    }

    public void setStatus(FamilyMemberStatus status) {
        this.status = status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
