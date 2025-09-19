package com.yonni.raquettelover.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PlaceOutDto(
        Long id,
        String codeLieu,
        String name,
        String address,
        LocalDateTime createdAt,
        List<CourtOutDto> courts
){}

