package com.yonni.raquettelover.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.yonni.raquettelover.dto.ApiResponse;
import com.yonni.raquettelover.dto.PlaceCreateDto;
import com.yonni.raquettelover.service.PlaceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/place") // Sans /api/v1, car context-path déjà /api/v1
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
public class PlaceController {
    
    private final PlaceService placeService;
    
    @PostMapping
    public ResponseEntity<?> addPlace(@RequestBody @Valid PlaceCreateDto dto,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Données invalides");
        }
        placeService.addPlace(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null, "Lieu créé avec succès"));
    }

    // recupère la liste des lieux de l'utilisateur
    // (pour un admin, ce sera tous les lieux)
    @GetMapping
    public ResponseEntity<?> getPlaces() {
        return ResponseEntity.ok(ApiResponse.success(placeService.getPlaces(), "Liste des lieux récupérée avec succès"));
    }
}
