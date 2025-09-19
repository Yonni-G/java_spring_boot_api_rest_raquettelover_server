package com.yonni.raquettelover.dto;

import com.yonni.raquettelover.enumeration.CourtType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CourtInDto(
                
        @NotBlank(message = "Le nom est obligatoire")
        String name,

        @NotBlank(message = "La description est obligatoire")
        String description,

        @NotNull(message = "Le type de terrain est obligatoire")
        CourtType type,

        @NotNull(message = "L'ID de l'utilisateur est obligatoire")
        Long userId)
        {}

