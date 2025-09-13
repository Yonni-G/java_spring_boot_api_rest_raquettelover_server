package com.yonni.raquettelover.dto;

import java.time.LocalDateTime;

public record PlaceDto(
        Long id,
        String name,
        String address,
        LocalDateTime createdAt
        ){
}

