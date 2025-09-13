package com.yonni.raquettelover.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yonni.raquettelover.entity.Court;

public interface CourtRepository extends JpaRepository<Court, Long> {
}
