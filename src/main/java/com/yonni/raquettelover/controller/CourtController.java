package com.yonni.raquettelover.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.yonni.raquettelover.dto.ApiResponse;
import com.yonni.raquettelover.dto.CourtInDto;
import com.yonni.raquettelover.security.ValidationUtil;
import com.yonni.raquettelover.service.CourtService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/place/{placeId}/court") // Sans /api/v1, car context-path déjà /api/v1
@RequiredArgsConstructor
public class CourtController {

    private final CourtService courtService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<?> createCourt(@PathVariable Long placeId, @RequestBody @Valid CourtInDto dto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(ValidationUtil.buildValidationError(bindingResult)));
        }

        courtService.createCourt(dto, placeId);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null, "Terrain créé avec succès"));
    }

}
