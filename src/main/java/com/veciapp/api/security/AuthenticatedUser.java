package com.veciapp.api.security;

public record AuthenticatedUser(Long userId, String email) {
}

