package com.yonni.raquettelover.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
//@JsonInclude(JsonInclude.Include.NON_NULL)
public record ParticipationGuestDto(
        @Valid
        GuestDto guest,
        @NotNull(message = "L'ID de la réservation est obligatoire")
        Long reservationId,
        @NotNull(message = "L'ID de l'utilisateur est obligatoire")
        Long userId // participant en propre ou inviteur si invité défini
) {
}