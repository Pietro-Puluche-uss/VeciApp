package com.veciapp.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.veciapp.api.dto.FamilyMapMemberResponse;
import com.veciapp.api.dto.FamilyMemberRequest;
import com.veciapp.api.dto.FamilyMemberResponse;
import com.veciapp.api.entity.FamilyMember;
import com.veciapp.api.entity.User;
import com.veciapp.api.exception.BadRequestException;
import com.veciapp.api.exception.ForbiddenException;
import com.veciapp.api.exception.ResourceNotFoundException;
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

    public List<FamilyMemberResponse> listMembers(Long userId) {
        return familyMemberRepository.findByOwnerIdOrderByCreatedAtAsc(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public FamilyMemberResponse addMember(Long userId, FamilyMemberRequest request) {
        User owner = profileService.loadUser(userId);
        if (owner.getSubscriptionPlan() != SubscriptionPlan.FAMILY) {
            throw new ForbiddenException("Necesitas el plan Familiar para agregar miembros");
        }
        if (familyMemberRepository.countByOwnerId(userId) >= FAMILY_MEMBER_LIMIT) {
            throw new BadRequestException("El plan Familiar admite hasta 5 miembros");
        }

        User member = userRepository.findByEmailIgnoreCase(request.email().trim())
                .orElseThrow(() -> new ResourceNotFoundException("No existe un usuario con ese correo"));
        if (member.getId().equals(owner.getId())) {
            throw new BadRequestException("No puedes agregarte a ti mismo");
        }
        if (familyMemberRepository.existsByOwnerIdAndMemberId(userId, member.getId())) {
            throw new BadRequestException("Ese usuario ya esta vinculado a tu familia");
        }

        FamilyMember familyMember = new FamilyMember();
        familyMember.setOwner(owner);
        familyMember.setMember(member);
        familyMember.setAlias(blankToNull(request.alias()));
        familyMember.setRelationshipLabel(blankToNull(request.relationshipLabel()));
        return toResponse(familyMemberRepository.save(familyMember));
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

    public List<FamilyMapMemberResponse> getFamilyMap(Long userId) {
        User owner = profileService.loadUser(userId);
        List<FamilyMapMemberResponse> members = new ArrayList<>();
        members.add(toMapResponse(owner, null, "Titular"));
        familyMemberRepository.findByOwnerIdOrderByCreatedAtAsc(userId)
                .forEach(member -> members.add(toMapResponse(
                        member.getMember(),
                        member.getAlias(),
                        member.getRelationshipLabel())));
        return members;
    }

    private FamilyMemberResponse toResponse(FamilyMember familyMember) {
        User member = familyMember.getMember();
        return new FamilyMemberResponse(
                familyMember.getId(),
                member.getId(),
                member.getFullName(),
                member.getEmail(),
                member.getPhone(),
                familyMember.getAlias(),
                familyMember.getRelationshipLabel(),
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

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}

