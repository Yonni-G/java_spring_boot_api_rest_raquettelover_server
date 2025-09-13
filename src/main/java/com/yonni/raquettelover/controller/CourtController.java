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
import com.yonni.raquettelover.dto.CourtDto;
import com.yonni.raquettelover.service.CourtService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/court") // Sans /api/v1, car context-path déjà /api/v1
@RequiredArgsConstructor
public class CourtController {

    private final CourtService courtService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping
    public ResponseEntity<?> addCourt(@RequestBody @Valid CourtDto dto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Données invalides");
        }

        courtService.addCourt(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null, "Terrain créé avec succès"));
    }

}
