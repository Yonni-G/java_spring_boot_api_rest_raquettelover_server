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
import com.yonni.raquettelover.dto.ParticipationGuestDto;
import com.yonni.raquettelover.dto.ParticipationPlayerDto;
import com.yonni.raquettelover.service.ParticipationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/participation")
@RequiredArgsConstructor
public class ParticipationController {

    private final ParticipationService participationService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addParticipation(
            @RequestBody @Valid ParticipationPlayerDto dto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Données invalides");
        }

        participationService.addParticipation(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null, "Participation créée avec succès"));
    }

}
