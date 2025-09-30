package com.yonni.raquettelover.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.yonni.raquettelover.entity.Place;
import com.yonni.raquettelover.entity.User;
import com.yonni.raquettelover.entity.UserSubscriptionPlace;
import com.yonni.raquettelover.exception.NotUniqueExceptionCustom;
import com.yonni.raquettelover.repository.PlaceRepository;
import com.yonni.raquettelover.repository.UserSubscriptionPlaceRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserSubscriptionPlaceServiceImpl implements UserSubscriptionPlaceService {
    
    private final UserSubscriptionPlaceRepository userSubscriptionPlaceRepository;
    private final PlaceRepository placeRepository;

    public void createSubscription(User user, Long placeId) {

        Place place = placeRepository.findById(placeId)
            .orElseThrow (() -> new EntityNotFoundException("Ce lieu n'existe pas !"));
        
        if (userSubscriptionPlaceRepository.existsByUserAndPlace(user, place)) {
            throw new NotUniqueExceptionCustom(null, "Souscription déjà existante pour cet utilisateur et ce lieu.");
        }

        userSubscriptionPlaceRepository.save(new UserSubscriptionPlace(user, place));
    }

}
