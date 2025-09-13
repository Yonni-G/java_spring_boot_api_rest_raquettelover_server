package com.yonni.raquettelover.dto;

import jakarta.validation.constraints.NotNull;

public record ParticipationPlayerDto(
        @NotNull(message = "L'ID de la réservation est obligatoire")
        Long reservationId,
        Long userId // participant de la réservation
) {
}

