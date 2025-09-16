package com.yonni.raquettelover.service;

import org.springframework.stereotype.Service;

import com.yonni.raquettelover.dto.CourtInDto;

@Service
public interface CourtService {
    public void createCourt(CourtInDto dto, Long placId);
}
