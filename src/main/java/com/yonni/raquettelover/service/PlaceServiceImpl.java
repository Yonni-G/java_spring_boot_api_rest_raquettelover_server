package com.yonni.raquettelover.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.yonni.raquettelover.dto.ApiResponse;
import com.yonni.raquettelover.dto.PlaceCreateDto;
import com.yonni.raquettelover.dto.PlaceDto;
import com.yonni.raquettelover.entity.Place;
import com.yonni.raquettelover.entity.User;
import com.yonni.raquettelover.entity.UserPlace;
import com.yonni.raquettelover.exception.AccessDeniedExceptionCustom;
import com.yonni.raquettelover.repository.PlaceRepository;
import com.yonni.raquettelover.repository.UserPlaceRepository;
import com.yonni.raquettelover.repository.UserRepository;
import com.yonni.raquettelover.security.CustomUserDetails;
import com.yonni.raquettelover.security.SecurityUtils;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final UserPlaceRepository userPlaceRepository;
    private final UserService userService;

    @Override
    public void addPlace(PlaceCreateDto dto) {

        // un admin peut créer un lieu pour un autre utilisateur
        // mais un manager ne le peut que pour lui-même

        CustomUserDetails principal = SecurityUtils.getCurrentUser();
        if (userService.hasRoleManager(principal)) {
            if(!dto.userId().equals(principal.getId())) {
                // on envoit une response entity avec une response error
                throw new AccessDeniedExceptionCustom("Accès refusé : Vous ne pas créer un lieu pour quelqu'un d'autre que vous");
            }
        }
        // utilisateur
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Place place = new Place();
        place.setName(dto.name());
        place.setAddress(dto.address());
        Place placeAdded = placeRepository.save(place);

        userPlaceRepository.save(new UserPlace(user, placeAdded));
    }

    @Override
    public List<PlaceDto> getPlaces() {
        CustomUserDetails principal = SecurityUtils.getCurrentUser();
        List<Place> places;

        if (userService.hasRoleAdmin(principal)) {
            places = placeRepository.findAll();
        } else if (userService.hasRoleManager(principal)) {
            places = userPlaceRepository.findPlacesByUserId(principal.getId());
        } else {
            throw new AccessDeniedExceptionCustom("Accès refusé : Vous n'avez pas suffisamment de droits");
        }

        return places.stream()
                .map(place -> new PlaceDto(place.getId(), place.getName(), place.getAddress(), place.getCreatedAt()))
                .toList();
    }

}
