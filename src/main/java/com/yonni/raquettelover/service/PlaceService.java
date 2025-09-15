package com.yonni.raquettelover.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.yonni.raquettelover.dto.PlaceCreateDto;
import com.yonni.raquettelover.dto.PlaceDto;

@Service
public interface PlaceService {
    void createPlace(PlaceCreateDto dto);
    void updatePlace(PlaceCreateDto dto, Long id);

    List<PlaceDto> getPlaces();
    Optional<PlaceDto> getById(Long id);
}
