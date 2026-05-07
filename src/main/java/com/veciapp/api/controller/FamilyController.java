package com.veciapp.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.veciapp.api.dto.ApiMessageResponse;
import com.veciapp.api.dto.FamilyInvitationResponse;
import com.veciapp.api.dto.FamilyMapMemberResponse;
import com.veciapp.api.dto.FamilyMemberRequest;
import com.veciapp.api.dto.FamilyMemberResponse;
import com.veciapp.api.security.SecurityUtils;
import com.veciapp.api.service.FamilyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/family")
public class FamilyController {

    private final FamilyService familyService;

    public FamilyController(FamilyService familyService) {
        this.familyService = familyService;
    }

    @GetMapping("/members")
    public List<FamilyMemberResponse> listMembers(Authentication authentication) {
        return familyService.listMembers(SecurityUtils.currentUserId(authentication));
    }

    @GetMapping("/invitations/mine")
    public List<FamilyInvitationResponse> listMyInvitations(Authentication authentication) {
        return familyService.listMyInvitations(SecurityUtils.currentUserId(authentication));
    }

    @PostMapping("/members")
    @ResponseStatus(HttpStatus.CREATED)
    public FamilyMemberResponse addMember(
            Authentication authentication,
            @Valid @RequestBody FamilyMemberRequest request) {
        return familyService.addMember(SecurityUtils.currentUserId(authentication), request);
    }

    @PostMapping("/invitations/{id}/accept")
    public ApiMessageResponse acceptInvitation(
            Authentication authentication,
            @PathVariable Long id) {
        familyService.acceptInvitation(SecurityUtils.currentUserId(authentication), id);
        return new ApiMessageResponse("Invitacion aceptada");
    }

    @PostMapping("/invitations/{id}/reject")
    public ApiMessageResponse rejectInvitation(
            Authentication authentication,
            @PathVariable Long id) {
        familyService.rejectInvitation(SecurityUtils.currentUserId(authentication), id);
        return new ApiMessageResponse("Invitacion rechazada");
    }

    @DeleteMapping("/members/{id}")
    public ApiMessageResponse removeMember(
            Authentication authentication,
            @PathVariable Long id) {
        familyService.removeMember(SecurityUtils.currentUserId(authentication), id);
        return new ApiMessageResponse("Miembro familiar eliminado");
    }

    @DeleteMapping("/members/me")
    public ApiMessageResponse leaveGroup(Authentication authentication) {
        familyService.leaveGroup(SecurityUtils.currentUserId(authentication));
        return new ApiMessageResponse("Saliste del grupo");
    }

    @GetMapping("/map")
    public List<FamilyMapMemberResponse> getFamilyMap(Authentication authentication) {
        return familyService.getFamilyMap(SecurityUtils.currentUserId(authentication));
    }
}
