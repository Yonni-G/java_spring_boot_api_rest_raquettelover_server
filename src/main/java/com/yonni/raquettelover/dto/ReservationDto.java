package com.yonni.raquettelover.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ReservationDto(
        @NotNull(message = "La date de réservation est obligatoire")
        LocalDate reservationAt,

        @NotNull(message = "L'heure de début est obligatoire")
        @Min(value = 0, message = "L'heure de début doit être comprise entre 0 et 23")
        @Max(value = 23, message = "L'heure de début doit être comprise entre 0 et 23")
        Integer startHour,

        @NotNull(message = "La durée est obligatoire")
        @Min(value = 1, message = "La durée minimale est d'une heure")
        @Max(value = 24, message = "La durée maximale est de 24 heures")
        Integer duration,

        @NotNull(message = "L'ID du court est obligatoire")
        Long courtId,
        Long userId, // joueur créant la réservation
        // au moment de la réservation, le joueur peut ajouter des invités (optionnel)
        @Valid
        List<GuestDto> guests
) {
}

