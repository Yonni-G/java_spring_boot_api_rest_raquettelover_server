package com.yonni.raquettelover.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


public class SecurityUtils {

    private SecurityUtils() {
        // Constructeur privé pour empêcher l'instanciation
    }

    public static CustomUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new IllegalStateException("Utilisateur non authentifié");
        }
        return (CustomUserDetails) authentication.getPrincipal();
    }
}

