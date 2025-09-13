package com.yonni.raquettelover.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yonni.raquettelover.dto.ApiResponse;
import com.yonni.raquettelover.dto.CourtTypeDto;
import com.yonni.raquettelover.enumeration.CourtType;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/court-type") // Sans /api/v1, car context-path déjà /api/v1
@RequiredArgsConstructor
public class CourtTypeController {

        @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
        @GetMapping
        public ResponseEntity<ApiResponse<List<CourtTypeDto>>> getCourtTypes() {
                List<CourtTypeDto> courtTypes = Arrays.stream(CourtType.values())
                        .map(ct -> new CourtTypeDto(ct.name(), ct.getLabel(), ct.getMinPlayers()))
                        .toList();                

                return ResponseEntity.ok(ApiResponse.success(courtTypes, "Liste des types de courts"));
        }
}

