package com.yonni.raquettelover.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.yonni.raquettelover.dto.CourtInDto;
import com.yonni.raquettelover.entity.Court;
import com.yonni.raquettelover.entity.Place;
import com.yonni.raquettelover.entity.User;
import com.yonni.raquettelover.repository.CourtRepository;
import com.yonni.raquettelover.repository.PlaceRepository;
import com.yonni.raquettelover.repository.UserPlaceRepository;
import com.yonni.raquettelover.repository.UserRepository;
import com.yonni.raquettelover.security.CustomUserDetails;
import com.yonni.raquettelover.security.SecurityUtils;

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lieu non trouvé"));

        // seuls les admins ou managers du lieu peuvent ajouter un terrain
        // si l'utilisateur est manager, on vérifie qu'il gère bien le lieu

        if (userService.hasRoleManager(principal)
                && !userPlaceRepository.existsByUserIdAndPlaceId(principal.getId(), placeId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès refusé");
        }

        Court court = new Court();
        court.setName(dto.name());
        court.setDescription(dto.description());
        court.setType(dto.type());
        // on associe le terrain au lieu
        court.setPlace(place);

        courtRepository.save(court);
    }

}
