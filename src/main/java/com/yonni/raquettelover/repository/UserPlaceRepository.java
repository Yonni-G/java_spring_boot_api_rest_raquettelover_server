package com.yonni.raquettelover.repository;

import com.yonni.raquettelover.entity.Place;
import com.yonni.raquettelover.entity.User;
import com.yonni.raquettelover.entity.UserPlace;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserPlaceRepository extends JpaRepository<UserPlace, Long> {
    boolean existsByUserAndPlace(User user, Place place);
    boolean existsByUserIdAndPlaceId(Long userId, Long placeId);
    
    @Query("SELECT up.place FROM UserPlace up WHERE up.user.id = :userId")
    List<Place> findPlacesByUserId(@Param("userId") Long userId);
}
