package com.yonni.raquettelover.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yonni.raquettelover.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
