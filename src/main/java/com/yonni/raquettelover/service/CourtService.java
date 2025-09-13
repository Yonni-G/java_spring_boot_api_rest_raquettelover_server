package com.yonni.raquettelover.service;

import org.springframework.stereotype.Service;

import com.yonni.raquettelover.dto.CourtDto;

@Service
public interface CourtService {
    public void addCourt(CourtDto dto);
}
