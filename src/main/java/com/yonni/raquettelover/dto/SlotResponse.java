package com.yonni.raquettelover.dto;

import java.time.LocalTime;

import com.yonni.raquettelover.enumeration.CourtType;

public record SlotResponse(
        CourtType type,
        LocalTime startTime,
        LocalTime endTime,
        Integer freeCourts
        ) {
}
