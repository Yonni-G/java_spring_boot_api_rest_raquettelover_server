package com.yonni.raquettelover.mapper;

import com.yonni.raquettelover.dto.PlaceOutDto;
import com.yonni.raquettelover.entity.Place;

public class PlaceMapper {
    private PlaceMapper() {}
    
    public static PlaceOutDto toDto(Place place) {
        return new PlaceOutDto(
                place.getId(),
                place.getName(),
                place.getAddress(),
                place.getCreatedAt());
    }
}
