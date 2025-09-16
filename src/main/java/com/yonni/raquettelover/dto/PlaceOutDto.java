package com.yonni.raquettelover.dto;

import java.time.LocalDateTime;

public record PlaceOutDto(
        Long id,
        String name,
        String address,
        LocalDateTime createdAt
        ){
}

