package com.yonni.raquettelover.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.yonni.raquettelover.dto.PlaceInDto;
import com.yonni.raquettelover.dto.PlaceOutDto;

@Service
public interface PlaceService {
    void createPlace(PlaceInDto dto);
    void updatePlace(PlaceInDto dto, Long id);
    List<PlaceOutDto> getPlaces();
}
