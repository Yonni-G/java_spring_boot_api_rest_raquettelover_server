package com.yonni.raquettelover.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PlaceCreateDto(
        @NotNull(message = "L'ID de l'utilisateur est obligatoire")
        Long userId,
        @NotBlank(message = "Le nom est obligatoire")
        String name,

        @NotBlank(message = "L'adresse est obligatoire")
        String address){
}

