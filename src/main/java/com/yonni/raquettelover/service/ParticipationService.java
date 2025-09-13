package com.yonni.raquettelover.service;

import com.yonni.raquettelover.dto.ParticipationGuestDto;
import com.yonni.raquettelover.dto.ParticipationPlayerDto;
import org.springframework.stereotype.Service;

@Service
public interface ParticipationService {
    public void addParticipation(ParticipationPlayerDto dto);
    public void addParticipation(ParticipationGuestDto dto);
}
