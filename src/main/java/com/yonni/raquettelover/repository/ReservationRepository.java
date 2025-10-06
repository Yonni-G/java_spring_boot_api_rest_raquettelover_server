package com.yonni.raquettelover.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yonni.raquettelover.entity.Court;
import com.yonni.raquettelover.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    public boolean existsByCourtAndReservationAtAndStartHourBetween(Court court, LocalDate reservationAt, Integer startHour, Integer endHour);
}
