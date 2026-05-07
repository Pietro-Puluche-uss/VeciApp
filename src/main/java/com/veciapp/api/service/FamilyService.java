package com.veciapp.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.veciapp.api.dto.FamilyInvitationResponse;
import com.veciapp.api.dto.FamilyMapMemberResponse;
import com.veciapp.api.dto.FamilyMemberRequest;
import com.veciapp.api.dto.FamilyMemberResponse;
import com.veciapp.api.entity.FamilyMember;
import com.veciapp.api.entity.User;
import com.veciapp.api.exception.BadRequestException;
import com.veciapp.api.exception.ForbiddenException;
import com.veciapp.api.exception.ResourceNotFoundException;
import com.veciapp.api.model.FamilyGroupType;
import com.veciapp.api.model.FamilyMemberStatus;
import com.veciapp.api.model.SubscriptionPlan;
import com.veciapp.api.repository.FamilyMemberRepository;
import com.veciapp.api.repository.UserRepository;

@Service
public class FamilyService {

    private static final int FAMILY_MEMBER_LIMIT = 5;

    private final FamilyMemberRepository familyMemberRepository;
    private final UserRepository userRepository;
    private final ProfileService profileService;

    public FamilyService(
            FamilyMemberRepository familyMemberRepository,
            UserRepository userRepository,
            ProfileService profileService) {
        this.familyMemberRepository = familyMemberRepository;
        this.userRepository = userRepository;
        this.profileService = profileService;
    }

    @Transactional(readOnly = true)
    public List<FamilyMemberResponse> listMembers(Long userId) {
        User ownerContext = resolveOwnerContext(userId);
        if (ownerContext == null) {
            return List.of();
        }
        return familyMemberRepository.findByOwnerIdOrderByCreatedAtAsc(ownerContext.getId()).stream()
                .filter(this::isAccepted)
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FamilyInvitationResponse> listMyInvitations(Long userId) {
        return familyMemberRepository.findByMemberIdOrderByCreatedAtAsc(userId).stream()
                .filter(this::isPending)
                .map(this::toInvitationResponse)
                .toList();
    }

    @Transactional
    public FamilyMemberResponse addMember(Long userId, FamilyMemberRequest request) {
        User owner = profileService.loadUser(userId);
        if (owner.getSubscriptionPlan() != SubscriptionPlan.FAMILY) {
            throw new ForbiddenException("Necesitas el plan Familiar para agregar miembros");
        }
        long activeMembers = familyMemberRepository.findByOwnerIdOrderByCreatedAtAsc(userId).stream()
                .filter(this::isAccepted)
                .count();
        if (activeMembers >= FAMILY_MEMBER_LIMIT) {
            throw new BadRequestException("El plan Familiar admite hasta 5 miembros");
        }

        User member = userRepository.findByEmailIgnoreCase(request.email().trim())
                .orElseThrow(() -> new ResourceNotFoundException("No existe un usuario con ese correo"));
        if (member.getId().equals(owner.getId())) {
            throw new BadRequestException("No puedes agregarte a ti mismo");
        }
        if (familyMemberRepository.existsByOwnerIdAndMemberId(userId, member.getId())) {
            throw new BadRequestException("Ese usuario ya tiene una invitacion o ya forma parte de tu grupo");
        }
        if (hasAcceptedMembershipInAnotherGroup(member.getId(), owner.getId())) {
            throw new BadRequestException("Ese usuario ya participa en otro grupo");
        }

        FamilyMember familyMember = new FamilyMember();
        familyMember.setOwner(owner);
        familyMember.setMember(member);
        familyMember.setAlias(blankToNull(request.alias()));
        familyMember.setRelationshipLabel(blankToNull(request.relationshipLabel()));
        familyMember.setGroupType(FamilyGroupType.fromValue(request.groupType()));
        familyMember.setStatus(FamilyMemberStatus.PENDING);
        return toResponse(familyMemberRepository.save(familyMember));
    }

    @Transactional
    public void acceptInvitation(Long userId, Long invitationId) {
        FamilyMember invitation = loadInvitation(invitationId);
        if (!invitation.getMember().getId().equals(userId)) {
            throw new ForbiddenException("No autorizado para aceptar esta invitacion");
        }
        if (!isPending(invitation)) {
            throw new BadRequestException("La invitacion ya no esta pendiente");
        }
        if (hasAcceptedMembershipInAnotherGroup(userId, invitation.getOwner().getId())) {
            throw new BadRequestException("Ya participas en otro grupo");
        }
        invitation.setStatus(FamilyMemberStatus.ACCEPTED);
        familyMemberRepository.save(invitation);
    }

    @Transactional
    public void rejectInvitation(Long userId, Long invitationId) {
        FamilyMember invitation = loadInvitation(invitationId);
        if (!invitation.getMember().getId().equals(userId)) {
            throw new ForbiddenException("No autorizado para rechazar esta invitacion");
        }
        if (!isPending(invitation)) {
            throw new BadRequestException("La invitacion ya no esta pendiente");
        }
        familyMemberRepository.delete(invitation);
    }

    @Transactional
    public void removeMember(Long userId, Long familyMemberId) {
        FamilyMember familyMember = familyMemberRepository.findById(familyMemberId)
                .orElseThrow(() -> new ResourceNotFoundException("Miembro familiar no encontrado"));
        if (!familyMember.getOwner().getId().equals(userId)) {
            throw new ForbiddenException("No autorizado para eliminar este miembro");
        }
        familyMemberRepository.delete(familyMember);
    }

    @Transactional
    public void leaveGroup(Long userId) {
        FamilyMember membership = familyMemberRepository.findByMemberIdOrderByCreatedAtAsc(userId).stream()
                .filter(this::isAccepted)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No perteneces a un grupo activo"));
        familyMemberRepository.delete(membership);
    }

    @Transactional(readOnly = true)
    public List<FamilyMapMemberResponse> getFamilyMap(Long userId) {
        User ownerContext = resolveOwnerContext(userId);
        if (ownerContext == null) {
            return List.of();
        }
        List<FamilyMapMemberResponse> members = new ArrayList<>();
        members.add(toMapResponse(ownerContext, null, "Titular"));
        familyMemberRepository.findByOwnerIdOrderByCreatedAtAsc(ownerContext.getId()).stream()
                .filter(this::isAccepted)
                .forEach(member -> members.add(toMapResponse(
                        member.getMember(),
                        member.getAlias(),
                        member.getRelationshipLabel())));
        return members;
    }

    private User resolveOwnerContext(Long userId) {
        User currentUser = profileService.loadUser(userId);
        if (currentUser.getSubscriptionPlan() == SubscriptionPlan.FAMILY) {
            return currentUser;
        }
        return familyMemberRepository.findByMemberIdOrderByCreatedAtAsc(userId).stream()
                .filter(this::isAccepted)
                .map(FamilyMember::getOwner)
                .findFirst()
                .orElse(null);
    }

    private boolean hasAcceptedMembershipInAnotherGroup(Long memberUserId, Long ownerToIgnore) {
        return familyMemberRepository.findByMemberIdOrderByCreatedAtAsc(memberUserId).stream()
                .filter(this::isAccepted)
                .anyMatch(member -> !Objects.equals(member.getOwner().getId(), ownerToIgnore));
    }

    private FamilyMember loadInvitation(Long invitationId) {
        return familyMemberRepository.findById(invitationId)
                .orElseThrow(() -> new ResourceNotFoundException("Invitacion no encontrada"));
    }

    private FamilyMemberResponse toResponse(FamilyMember familyMember) {
        User member = familyMember.getMember();
        return new FamilyMemberResponse(
                familyMember.getId(),
                familyMember.getOwner().getId(),
                member.getId(),
                member.getFullName(),
                member.getEmail(),
                member.getPhone(),
                familyMember.getAlias(),
                familyMember.getRelationshipLabel(),
                familyMember.getGroupType().name(),
                familyMember.getCreatedAt());
    }

    private FamilyInvitationResponse toInvitationResponse(FamilyMember familyMember) {
        User owner = familyMember.getOwner();
        return new FamilyInvitationResponse(
                familyMember.getId(),
                owner.getId(),
                owner.getFullName(),
                owner.getEmail(),
                familyMember.getAlias(),
                familyMember.getRelationshipLabel(),
                familyMember.getGroupType().name(),
                familyMember.getCreatedAt());
    }

    private FamilyMapMemberResponse toMapResponse(User user, String alias, String relationshipLabel) {
        return new FamilyMapMemberResponse(
                user.getId(),
                user.getFullName(),
                alias,
                relationshipLabel,
                user.getCurrentLatitude(),
                user.getCurrentLongitude(),
                user.getDistrict(),
                user.getCity(),
                user.getProfilePhotoUrl());
    }

    private boolean isAccepted(FamilyMember familyMember) {
        return familyMember.getStatus() == FamilyMemberStatus.ACCEPTED;
    }

    private boolean isPending(FamilyMember familyMember) {
        return familyMember.getStatus() == FamilyMemberStatus.PENDING;
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
