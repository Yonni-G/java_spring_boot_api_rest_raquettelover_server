package com.yonni.raquettelover.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PlaceInDto(
        @NotBlank(message = "Le Code Lieu est obligatoire")
        String codeLieu,

        @NotBlank(message = "Le nom est obligatoire")
        String name,

        @NotBlank(message = "L'adresse est obligatoire")
        String address,
        
        @NotNull(message = "L'ID de l'utilisateur est obligatoire")
        Long userId)
        //Long placeId : absent pour le CREATE ou fournit par l'URL pour l'UPDATE)
{}

