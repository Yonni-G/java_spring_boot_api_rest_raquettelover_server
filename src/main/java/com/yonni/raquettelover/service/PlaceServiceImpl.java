package com.yonni.raquettelover.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.yonni.raquettelover.dto.PlaceInDto;
import com.yonni.raquettelover.dto.PlaceOutDto;
import com.yonni.raquettelover.entity.Place;
import com.yonni.raquettelover.entity.User;
import com.yonni.raquettelover.entity.UserPlace;
import com.yonni.raquettelover.exception.AccessDeniedExceptionCustom;
import com.yonni.raquettelover.exception.NotUniqueExceptionCustom;
import com.yonni.raquettelover.mapper.PlaceMapper;
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
    public Place findByCodeLieu(String codeLieu) {
        return placeRepository.findByCodeLieu(codeLieu).orElseThrow(() -> new EntityNotFoundException("Le Code Lieu que vous avez saisi n'existe pas"));
    }

    @Override
    public void createPlace(PlaceInDto dto) {

        // un admin peut créer un lieu pour un autre utilisateur
        // mais un manager ne le peut que pour lui-même
        CustomUserDetails principal = SecurityUtils.getCurrentUser();
        if (userService.hasRoleManager(principal)) {
            if (!dto.userId().equals(principal.getId())) {
                // on envoit une response entity avec une response error
                throw new AccessDeniedExceptionCustom(
                        "Accès refusé : Vous ne pas créer un lieu pour quelqu'un d'autre que vous");
            }
        }

        placeRepository.findByCodeLieu(dto.codeLieu())
                .ifPresent(place -> {
                    throw new NotUniqueExceptionCustom(
                            "codeLieu",
                            "Code Lieu déjà pris, veuillez choisir un autre nom pour votre Code Lieu");
                });

        // utilisateur
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));

        Place place = new Place();
        place.setCodeLieu(dto.codeLieu());
        place.setName(dto.name());
        place.setAddress(dto.address());
        Place placeAdded = placeRepository.save(place);

        userPlaceRepository.save(new UserPlace(user, placeAdded));
    }

    @Override
    public void updatePlace(PlaceInDto dto, Long placeId) {

        // un admin peut modifier un lieu pour un autre utilisateur
        // mais un manager ne le peut que pour lui-même
        CustomUserDetails principal = SecurityUtils.getCurrentUser();
        if (userService.hasRoleManager(principal)) {
            if (!dto.userId().equals(principal.getId())) {
                // on envoit une response entity avec une response error
                throw new AccessDeniedExceptionCustom(
                        "Accès refusé : Vous ne pas modifier un lieu pour quelqu'un d'autre que vous");
            }
        }

        placeRepository.findByCodeLieu(dto.codeLieu())
                .ifPresent(place -> {
                    throw new NotUniqueExceptionCustom(
                            "codeLieu",
                            "Code Lieu déjà pris, veuillez choisir un autre nom pour votre Code Lieu");
                });
                
        // utilisateur
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));

        // On va chercher la Place
        Place place = placeRepository.findById(
                placeId)
                .orElseThrow(() -> new EntityNotFoundException("Place introuvable"));

        place.setName(dto.name());
        place.setAddress(dto.address());
        place.setCodeLieu(dto.codeLieu());
        Place placeAdded = placeRepository.save(place);

        userPlaceRepository.save(new UserPlace(user, placeAdded));
    }

    @Override
    public List<PlaceOutDto> getPlaces() {
        CustomUserDetails principal = SecurityUtils.getCurrentUser();
        List<Place> places;

        if (userService.hasRoleAdmin(principal)) {
            places = placeRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        } else if (userService.hasRoleManager(principal)) {
            places = userPlaceRepository.findPlacesByUserId(principal.getId());
        } else {
            throw new AccessDeniedExceptionCustom("Accès refusé : Vous n'avez pas suffisamment de droits");
        }

        // on transforme nos places en dto
        return places.stream()
                .map(PlaceMapper::toDto)
                .toList();
    }
}
