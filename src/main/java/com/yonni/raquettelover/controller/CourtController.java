package com.yonni.raquettelover.controller;

import java.util.Optional;

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

import com.yonni.raquettelover.dto.ApiError;
import com.yonni.raquettelover.dto.ApiResponse;
import com.yonni.raquettelover.dto.CourtInDto;
import com.yonni.raquettelover.dto.PlaceInDto;
import com.yonni.raquettelover.entity.Court;
import com.yonni.raquettelover.entity.Place;
import com.yonni.raquettelover.mapper.CourtMapper;
import com.yonni.raquettelover.repository.CourtRepository;
import com.yonni.raquettelover.repository.PlaceRepository;
import com.yonni.raquettelover.security.ValidationUtil;
import com.yonni.raquettelover.service.CourtService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/place/{placeId}/court")
@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
public class CourtController {

    private final CourtService courtService;
    private final CourtRepository courtRepository;
    
    @PostMapping()
    public ResponseEntity<?> createCourt(@PathVariable Long placeId, @RequestBody @Valid CourtInDto dto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(ValidationUtil.buildValidationError(bindingResult)));
        }

        courtService.createCourt(dto, placeId);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null, "Court créé avec succès"));
    }

    
    @PutMapping("/{courtId}")
    public ResponseEntity<?> updatePlace(@PathVariable Long placeId,
            @PathVariable Long courtId,
            @RequestBody @Valid CourtInDto dto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(ValidationUtil.buildValidationError(bindingResult)));
        }
        
        courtService.updateCourt(dto, placeId, courtId);

        // retourner 204 No Content pour indiquer succès sans corps
        return ResponseEntity.ok().body(ApiResponse.success(null, "Modifications enregistrées avec succès"));
    }

    @GetMapping("/{courtId}")
    public ResponseEntity<?> findById(
            @PathVariable Long placeId,
            @PathVariable Long courtId) {
        Optional<Court> court = courtRepository.findById(courtId);

        if (court.isEmpty() || !court.get().getPlace().getId().equals(placeId)) {
            // Soit le court n’existe pas, soit il n’appartient pas à la place
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(new ApiError("COURT_NOT_FOUND", "Terrain non trouvé pour ce lieu")));
        }

        var dto = CourtMapper.toDto(court.get());

        return ResponseEntity.ok(ApiResponse.success(dto, "Terrain récupéré avec succès"));
    }

}
