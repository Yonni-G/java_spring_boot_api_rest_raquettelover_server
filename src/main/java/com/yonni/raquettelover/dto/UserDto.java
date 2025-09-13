package com.yonni.raquettelover.dto;

import jakarta.validation.constraints.Pattern;

public record UserDto(
        @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "L'adresse email saisie n'est pas valide"
        )
        String username,

        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?/~]).{8,}$",
                message = "Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial"
        )
        String password
) {}

