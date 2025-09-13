package com.yonni.raquettelover.repository;

import com.yonni.raquettelover.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
}
