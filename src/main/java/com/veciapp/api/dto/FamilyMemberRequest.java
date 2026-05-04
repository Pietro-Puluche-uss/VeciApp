package com.veciapp.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FamilyMemberRequest(
        @NotBlank @Email String email,
        @Size(max = 80) String alias,
        @Size(max = 60) String relationshipLabel) {
}

