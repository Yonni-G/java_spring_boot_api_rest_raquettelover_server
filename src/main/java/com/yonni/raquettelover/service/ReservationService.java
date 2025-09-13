package com.yonni.raquettelover.service;

import org.springframework.stereotype.Service;

import com.yonni.raquettelover.dto.ReservationDto;

@Service
public interface ReservationService {
    public void addReservation(ReservationDto dto);
}
