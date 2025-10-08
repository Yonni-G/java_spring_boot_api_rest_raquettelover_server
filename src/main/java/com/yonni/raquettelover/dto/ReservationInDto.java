package com.yonni.raquettelover.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record ReservationInDto(

        @NotNull(message = "L'heure de début est obligatoire")
        LocalDateTime startTime,

        @NotNull(message = "L'heure de fin est obligatoire")
        LocalDateTime endTime,

        @NotNull(message = "L'ID du court est obligatoire")
        Long courtId,
        Long userId, // joueur créant la réservation
        // au moment de la réservation, le joueur peut ajouter des invités (optionnel)
        @Valid
        List<GuestDto> guests
) {
}

