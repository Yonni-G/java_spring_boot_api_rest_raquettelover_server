package com.yonni.raquettelover.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.yonni.raquettelover.dto.ApiError;
import com.yonni.raquettelover.dto.ApiResponse;
import com.yonni.raquettelover.dto.PlaceCreateDto;
import com.yonni.raquettelover.dto.PlaceDto;
import com.yonni.raquettelover.repository.PlaceRepository;
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
    public ResponseEntity<?> createPlace(@RequestBody @Valid PlaceCreateDto dto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Données de création invalides");
        }
        placeService.createPlace(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null, "Création effectuée avec succès"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePlace(@PathVariable Long id, @RequestBody @Valid PlaceCreateDto dto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Données de modification invalides");
        }
        placeService.updatePlace(dto, id);

        // retourner 204 No Content pour indiquer succès sans corps
        return ResponseEntity.ok().body(ApiResponse.success(null, "Modifications enregistrées avec succès"));
    }

    // recupère la liste des lieux de l'utilisateur
    // (pour un admin, ce sera tous les lieux)
    @GetMapping
    public ResponseEntity<?> getPlaces() {
        return ResponseEntity
                .ok(ApiResponse.success(placeService.getPlaces(), "Liste des lieux récupérée avec succès"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return placeService.getById(id)
                .map(dto -> ResponseEntity.ok(ApiResponse.success(dto, "Lieu récupéré avec succès")))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(new ApiError("PLACE_NOT_FOUND", "Lieu non trouvé"))));
    }


}
