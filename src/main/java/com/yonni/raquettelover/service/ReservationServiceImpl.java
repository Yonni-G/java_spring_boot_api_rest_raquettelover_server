package com.yonni.raquettelover.service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.yonni.raquettelover.dto.ParticipationGuestDto;
import com.yonni.raquettelover.dto.ParticipationPlayerDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.yonni.raquettelover.dto.ReservationInDto;
import com.yonni.raquettelover.entity.Court;
import com.yonni.raquettelover.entity.Reservation;
import com.yonni.raquettelover.entity.User;
import com.yonni.raquettelover.exception.AccessDeniedExceptionCustom;
import com.yonni.raquettelover.exception.IllegalArgumentExceptionCustom;
import com.yonni.raquettelover.exception.NotUniqueExceptionCustom;
import com.yonni.raquettelover.repository.CourtRepository;
import com.yonni.raquettelover.repository.ReservationRepository;
import com.yonni.raquettelover.repository.UserPlaceRepository;
import com.yonni.raquettelover.repository.UserRepository;
import com.yonni.raquettelover.security.CustomUserDetails;
import com.yonni.raquettelover.security.SecurityUtils;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final UserRepository userRepository;
    private final ParticipationService participationService;
    private final CourtRepository courtRepository;
    private final ReservationRepository reservationRepository;
    private final UserPlaceRepository userPlaceRepository;
    private final UserService userService;

    @Override
    public void addReservation(ReservationInDto dto) {

        Court court = courtRepository.findById(dto.courtId())
                .orElseThrow(() -> new EntityNotFoundException("Court non trouvé"));

        CustomUserDetails principal = SecurityUtils.getCurrentUser();
        // un USER ne peut créer une réservation que pour lui-même
        // un ADMIN peut créer une réservation pour n'importe quel utilisateur
        // un MANAGER peut créer une réservation pour n'importe quel utilisateur mais
        // que pour un terrain qu'il gère

        if (principal.getId().equals(dto.userId())) {
            // le joueur crée une réservation pour lui-même, ok
        } else if (userService.hasRoleAdmin(principal)) {
            // un admin peut créer une réservation pour n'importe quel utilisateur, ok
        } else if (userService.hasRoleManager(principal)) {
            // un manager peut créer une réservation pour n'importe quel utilisateur mais
            // que pour un terrain qu'il gère
            boolean isManagerOfCourt = userPlaceRepository.existsByUserIdAndPlaceId(
                    principal.getId(), court.getPlace().getId());
            if (!isManagerOfCourt) {
                throw new AccessDeniedExceptionCustom("Accès refusé : vous ne gérez pas ce terrain");
            }
        } else {
            // ni le joueur lui-même, ni un admin, ni un manager
            throw new AccessDeniedExceptionCustom(
                    "Accès refusé : vous n'avez pas les droits suffisants pour effectuer cette action");
        }

        // on vérifie que l'utilisateur bénéficiaire de la réservation existe
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));

        // on controle les heures entieres
        validateWholeHour(dto.startTime(), "startTime");
        validateWholeHour(dto.endTime(), "endTime");

        // on vérifie que cette réservation est possible sur ce court sur le créneau demandé
        if (reservationRepository.existsByCourtAndStartTimeLessThanAndEndTimeGreaterThan(court, dto.endTime(),
                dto.startTime())) {
            throw new NotUniqueExceptionCustom(null, "Une réservation sur ce créneau existe déjà");
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setCourt(court);
        reservation.setStartTime(dto.startTime());
        reservation.setEndTime(dto.endTime());

        Reservation reservationNew = reservationRepository.save(reservation);

        // on ajoute le joueur qui réserve en tant que participant
        ParticipationPlayerDto participationPlayerDto = new ParticipationPlayerDto(
                reservationNew.getId(),
                user.getId());
        participationService.addParticipation(participationPlayerDto);

        // on ajoute les invités si présents dans le dto
        // on boucle sur la liste dto.guests()
        if (dto.guests() != null) {
            dto.guests().forEach(guestDto -> {
                ParticipationGuestDto participationGuestDto = new ParticipationGuestDto(
                        guestDto,
                        reservationNew.getId(), // on associe l'invité à la réservation créée
                        user.getId());
                participationService.addParticipation(participationGuestDto);
            });
        }
    }

    private boolean isWholeHour(LocalDateTime dateTime) {
        return dateTime.getMinute() == 0 && dateTime.getSecond() == 0 && dateTime.getNano() == 0;
    }

    private void validateWholeHour(LocalDateTime dateTime, String fieldName) {
        if (dateTime == null) {
            throw new IllegalArgumentExceptionCustom(fieldName, fieldName + " ne peut pas être nul");
        }
        if (!isWholeHour(dateTime)) {
            throw new IllegalArgumentExceptionCustom(fieldName,
                    "Le champ '" + fieldName + "' doit être une heure entière (ex : 14:00)");
        }
    }

}
