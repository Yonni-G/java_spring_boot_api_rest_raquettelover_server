package com.yonni.raquettelover.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yonni.raquettelover.entity.Court;
import com.yonni.raquettelover.enumeration.CourtType;

public interface CourtRepository extends JpaRepository<Court, Long> {
    List<Court> findByPlaceIdAndType(Long placeId, CourtType type);
    List<Court> findByPlaceId(Long placeId);
}
