package com.yonni.raquettelover.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.yonni.raquettelover.entity.Court;
import com.yonni.raquettelover.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    boolean existsByCourtAndStartTimeLessThanAndEndTimeGreaterThan(
            Court court,
            LocalDateTime endTime,
            LocalDateTime startTime);

    @Query("select r from Reservation r where r.court in :courts and DATE(startTime) = :date")
    List<Reservation> findByCourtInAndDate(List<Court> courts, LocalDate date);

}
