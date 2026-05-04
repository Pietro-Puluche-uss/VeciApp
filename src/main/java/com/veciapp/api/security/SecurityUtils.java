package com.veciapp.api.security;

import org.springframework.security.core.Authentication;

import com.veciapp.api.exception.UnauthorizedException;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static Long currentUserId(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof AuthenticatedUser user)) {
            throw new UnauthorizedException("Sesion invalida");
        }
        return user.userId();
    }
}

