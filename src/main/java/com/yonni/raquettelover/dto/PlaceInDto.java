package com.yonni.raquettelover.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PlaceInDto(
        @NotBlank(message = "Le Code Lieu est obligatoire")
        @Pattern(                     
                regexp = "^[a-z-]{2,50}$",
                message = "Le Code Lieu ne peut contenir que des minuscules et des tirets"
        )
        String codeLieu,

        @NotBlank(message = "Le nom est obligatoire")
        @Pattern(
                regexp = "^[0-9a-zA-Zà-žÀ-Ž' -]{2,50}$",
                message = "Ce champ n'est pas valide"
        )
        String name,

        @NotBlank(message = "L'adresse est obligatoire")
        @Pattern(regexp = "^.{10,100}$", message = "L'adresse doit faire entre 10 et 100 caractères")
        String address,
        
        @NotNull(message = "L'ID de l'utilisateur est obligatoire")
        Long userId)
        //Long placeId : absent pour le CREATE ou fournit par l'URL pour l'UPDATE)
{}

