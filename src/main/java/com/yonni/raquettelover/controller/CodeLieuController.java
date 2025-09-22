package com.yonni.raquettelover.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yonni.raquettelover.dto.ApiResponse;
import com.yonni.raquettelover.entity.Place;
import com.yonni.raquettelover.service.PlaceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/code-lieu") // Sans /api/v1, car context-path déjà /api/v1
@RequiredArgsConstructor
public class CodeLieuController {

    private final PlaceService placeService;

    @GetMapping("/{codeLieu}")
    public ResponseEntity<?> findByCodeLieu(@PathVariable String codeLieu) {
        Place place = placeService.findByCodeLieu(codeLieu);
        return ResponseEntity.ok().body(ApiResponse.success(place.getId(), null));
    }

}
