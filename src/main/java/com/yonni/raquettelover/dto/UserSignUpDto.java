package com.yonni.raquettelover.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserSignUpDto(
        // peut être null si l'inscrit est manager
        Long placeId,
        
        @NotBlank(message = "L'email ne peut pas être vide")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "L'adresse email saisie n'est pas valide") String username,

        @NotBlank(message = "Le mot de passe ne peut pas être vide")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?/~]).{8,}$", message = "Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial") String password,

        @NotBlank(message = "Le prénom ne peut pas être vide")
        @Size(min = 2, max = 50, message = "Le prénom doit contenir entre 2 et 50 caractères") String firstName,

        @NotBlank(message = "Le nom ne peut pas être vide")
        @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères") String lastName) {
}
