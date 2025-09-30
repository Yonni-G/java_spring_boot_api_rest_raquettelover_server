package com.yonni.raquettelover.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.yonni.raquettelover.dto.CourtInDto;
import com.yonni.raquettelover.entity.Court;
import com.yonni.raquettelover.entity.Place;
import com.yonni.raquettelover.exception.AccessDeniedExceptionCustom;
import com.yonni.raquettelover.repository.CourtRepository;
import com.yonni.raquettelover.repository.PlaceRepository;
import com.yonni.raquettelover.repository.UserPlaceRepository;
import com.yonni.raquettelover.security.CustomUserDetails;
import com.yonni.raquettelover.security.SecurityUtils;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourtServiceImpl implements CourtService {

    private final PlaceRepository placeRepository;
    private final UserPlaceRepository userPlaceRepository;
    private final CourtRepository courtRepository;
    private final UserService userService;

    @Override
    public void createCourt(CourtInDto dto, Long placeId) {

        CustomUserDetails principal = SecurityUtils.getCurrentUser();

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new EntityNotFoundException("Lieu non trouvé"));

        // seuls les admins ou managers du lieu peuvent ajouter un terrain
        // si l'utilisateur est admin, on vérifie que le dto.userId() est bien propriétaire du lieu
        // si l'utilisateur est manager, on vérifie qu'il gère bien le lieu

        if (userService.hasRoleAdmin(principal)
                && !userPlaceRepository.existsByUserIdAndPlaceId(dto.userId(), placeId)) {
            throw new AccessDeniedExceptionCustom(
                    "Accès refusé : L'utilisateur ne gére pas le lieu dans lequel vous souhaitez ajouter un court");
        }
        else if (userService.hasRoleManager(principal)
                && !userPlaceRepository.existsByUserIdAndPlaceId(principal.getId(), placeId)) {
            throw new AccessDeniedExceptionCustom("Accès refusé : Vous ne gérez pas le lieu dans lequel vous souhaitez ajouter un court");
        }

        Court court = new Court();
        court.setName(dto.name());
        court.setDescription(dto.description());
        court.setType(dto.type());
        // on associe le terrain au lieu
        court.setPlace(place);

        courtRepository.save(court);
    }

    public void updateCourt(CourtInDto dto, Long placeId, Long courtId) {
        CustomUserDetails principal = SecurityUtils.getCurrentUser();

        Optional<Court> court = courtRepository.findById(courtId);

        if (court.isEmpty() || !court.get().getPlace().getId().equals(placeId)) {
            // Soit le court n’existe pas, soit il n’appartient pas à la place
            throw new EntityNotFoundException("Terrain non trouvé pour ce lieu");
        }
        

        // seuls les admins ou managers du lieu peuvent modifier un terrain
        // si l'utilisateur est admin, on vérifie que le dto.userId() est bien
        // propriétaire du lieu
        // si l'utilisateur est manager, on vérifie qu'il gère bien le lieu

        if (userService.hasRoleAdmin(principal)
                && !userPlaceRepository.existsByUserIdAndPlaceId(dto.userId(), placeId)) {
            throw new AccessDeniedExceptionCustom(
                    "Accès refusé : L'utilisateur ne gére pas le lieu dans lequel vous souhaitez modifier un court");
        } else if (userService.hasRoleManager(principal)
                && !userPlaceRepository.existsByUserIdAndPlaceId(principal.getId(), placeId)) {
            throw new AccessDeniedExceptionCustom(
                    "Accès refusé : Vous ne gérez pas le lieu dans lequel vous souhaitez modifier un court");
        }

        court.get().setName(dto.name());
        court.get().setDescription(dto.description());
        court.get().setType(dto.type());

        courtRepository.save(court.get());
    }

}
