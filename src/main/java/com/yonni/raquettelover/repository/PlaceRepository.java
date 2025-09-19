package com.yonni.raquettelover.repository;

import com.yonni.raquettelover.entity.Place;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findByCodeLieu(String codeLieu);
}
