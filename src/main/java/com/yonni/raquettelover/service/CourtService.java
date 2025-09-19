package com.yonni.raquettelover.service;

import org.springframework.stereotype.Service;

import com.yonni.raquettelover.dto.CourtInDto;

@Service
public interface CourtService {
    void createCourt(CourtInDto dto, Long placeId);    
    void updateCourt(CourtInDto dto, Long placeId, Long courtId);
}
