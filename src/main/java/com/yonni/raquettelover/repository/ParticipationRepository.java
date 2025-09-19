package com.yonni.raquettelover.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yonni.raquettelover.entity.Participation;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    boolean existsByReservationIdAndUserId(Long reservationId, Long userId);
}
