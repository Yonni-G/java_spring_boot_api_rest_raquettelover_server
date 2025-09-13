package com.yonni.raquettelover.repository;

import com.yonni.raquettelover.entity.Place;
import com.yonni.raquettelover.entity.Reservation;
import com.yonni.raquettelover.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.yonni.raquettelover.entity.Participation;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    boolean existsByReservationIdAndUserId(Long reservationId, Long userId);
}
