package com.yonni.raquettelover.dto;

import jakarta.validation.constraints.*;

public record GuestDto(
        @Size(min = 2, max = 50)
        String firstName,
        // on ajoute une contrainte regex pour l'email
        @Pattern(
                regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                message = "L'adresse email doit être valide"
        )
        String email,
        @Size(min = 10, max = 10, message = "Le numéro de téléphone doit contenir exactement 10 chiffres")
        String phoneNumber) {
}

