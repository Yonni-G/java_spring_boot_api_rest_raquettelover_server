package com.yonni.raquettelover.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yonni.raquettelover.dto.PlaceCreateDto;
import com.yonni.raquettelover.dto.PlaceDto;

@Service
public interface PlaceService {
    public void addPlace(PlaceCreateDto dto);
    public List<PlaceDto> getPlaces();

}
