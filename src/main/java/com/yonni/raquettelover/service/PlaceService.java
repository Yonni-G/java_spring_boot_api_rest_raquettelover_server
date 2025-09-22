package com.yonni.raquettelover.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yonni.raquettelover.dto.PlaceInDto;
import com.yonni.raquettelover.dto.PlaceOutDto;
import com.yonni.raquettelover.entity.Place;

@Service
public interface PlaceService {
    void createPlace(PlaceInDto dto);
    void updatePlace(PlaceInDto dto, Long id);
    List<PlaceOutDto> getPlaces();
    Place findByCodeLieu(String codeLieu);
}
