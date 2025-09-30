package com.yonni.raquettelover.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yonni.raquettelover.entity.Place;
import com.yonni.raquettelover.entity.PlaceUserId;
import com.yonni.raquettelover.entity.User;
import com.yonni.raquettelover.entity.UserSubscriptionPlace;

public interface UserSubscriptionPlaceRepository extends JpaRepository<UserSubscriptionPlace, PlaceUserId> {
    boolean existsByUserAndPlace(User user, Place place);
}
