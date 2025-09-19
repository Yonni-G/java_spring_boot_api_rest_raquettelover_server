package com.yonni.raquettelover.mapper;

import java.util.List;

import com.yonni.raquettelover.dto.CourtOutDto;
import com.yonni.raquettelover.dto.PlaceOutDto;
import com.yonni.raquettelover.entity.Place;

public class PlaceMapper {
    private PlaceMapper() {
    }

    public static PlaceOutDto toDto(Place place) {
        // Conversion des courts associés à la place en DTOs
        List<CourtOutDto> courts = place.getCourts()
                .stream()
                .map(CourtMapper::toDto) // Appelle la méthode toDto du CourtMapper
                .toList();

        return new PlaceOutDto(
                place.getId(),
                place.getCodeLieu(),
                place.getName(),
                place.getAddress(),
                place.getCreatedAt(),
                courts 
        );
    }
}
