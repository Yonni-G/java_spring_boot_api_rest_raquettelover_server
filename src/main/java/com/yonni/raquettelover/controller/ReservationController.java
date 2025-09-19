package com.yonni.raquettelover.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.yonni.raquettelover.dto.ApiResponse;
import com.yonni.raquettelover.dto.ReservationDto;
import com.yonni.raquettelover.service.ReservationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reservation") // Sans /api/v1, car context-path déjà /api/v1
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addReservation(
            @RequestBody @Valid ReservationDto dto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Données invalides");
        }

        // on ajoute la réservation
        reservationService.addReservation(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null, "Réservation créée avec succès"));
    }

}
