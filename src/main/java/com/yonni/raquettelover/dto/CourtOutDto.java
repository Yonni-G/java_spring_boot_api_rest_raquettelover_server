package com.yonni.raquettelover.dto;

import java.time.LocalDateTime;

public record CourtOutDto(
        Long id,
        String name,
        String description,
        CourtTypeOutDto type,
        LocalDateTime createdAt
){}

