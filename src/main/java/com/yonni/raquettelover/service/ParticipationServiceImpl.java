package com.yonni.raquettelover.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.yonni.raquettelover.dto.ParticipationGuestDto;
import com.yonni.raquettelover.dto.ParticipationPlayerDto;
import com.yonni.raquettelover.entity.Participation;
import com.yonni.raquettelover.entity.Reservation;
import com.yonni.raquettelover.entity.User;
import com.yonni.raquettelover.exception.AccessDeniedExceptionCustom;
import com.yonni.raquettelover.exception.NotUniqueExceptionCustom;
import com.yonni.raquettelover.repository.ParticipationRepository;
import com.yonni.raquettelover.repository.ReservationRepository;
import com.yonni.raquettelover.repository.UserPlaceRepository;
import com.yonni.raquettelover.repository.UserRepository;
import com.yonni.raquettelover.security.CustomUserDetails;
import com.yonni.raquettelover.security.SecurityUtils;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParticipationServiceImpl implements ParticipationService {
    private final UserRepository userRepository;
    private final UserPlaceRepository userPlaceRepository;
    private final ParticipationRepository participationRepository;
    private final ReservationRepository reservationRepository;
    private final UserService userService;

    @Override
    public void addParticipation(ParticipationPlayerDto dto) {

        Optional<Reservation> reservationOpt = reservationRepository.findById(dto.reservationId());
        if (reservationOpt.isEmpty()) {
            throw new EntityNotFoundException("Participation non trouvée");
        }

        CustomUserDetails principal = SecurityUtils.getCurrentUser();

        // un USER ne peut créer une participation que pour lui-même
        // un ADMIN peut créer une participation pour n'importe quel utilisateur
        // un MANAGER peut créer une participation pour n'importe quel utilisateur mais que pour un terrain qu'il gère

        if (principal.getId().equals(dto.userId())) {
            // le joueur crée une participation pour lui-même, ok
        } else if (userService.hasRoleAdmin(principal)) {
            // un admin peut créer une participation pour n'importe quel utilisateur, ok
        } else if (userService.hasRoleManager(principal)) {
            // un manager peut créer une participation pour n'importe quel utilisateur mais que pour une réservation qui est liée à un terrain qu'il gère
            boolean isManagerOfCourt = userPlaceRepository.existsByUserIdAndPlaceId(
                    principal.getId(), reservationOpt.get().getCourt().getPlace().getId());
            if (!isManagerOfCourt) {
                throw new AccessDeniedExceptionCustom("Accès refusé : vous ne gérez pas ce terrain");
            }
        } else {
            // ni le joueur lui-même, ni un admin, ni un manager
            throw new AccessDeniedExceptionCustom("Accès refusé");
        }

        // on vérifie que l'utilisateur bénéficiaire de la réservation existe
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));


        // on vérifie que l'utilisateur n'est pas déjà inscrit à cette réservation
        if(participationRepository.existsByReservationIdAndUserId(dto.reservationId(), dto.userId())){
            throw new NotUniqueExceptionCustom(null, "Utilisateur déjà inscrit à cette réservation");
        }

        // participation pour un utilisateur enregistré
        Participation participation = new Participation();
        participation.setReservation(reservationOpt.get());
        participation.setUser(user);
        participation.setGuest(false);
        participationRepository.save(participation);
    }

    @Override
    public void addParticipation(ParticipationGuestDto dto) {

        Optional<Reservation> reservationOpt = reservationRepository.findById(dto.reservationId());
        if (reservationOpt.isEmpty()) {
            throw new EntityNotFoundException("Participation non trouvée");
        }

        // participation pour un invité
        Participation participation = new Participation();
        participation.setReservation(reservationOpt.get());
        // on associe l'invité à l'utilisateur qui l'ajoute
        participation.setUser(userRepository.findById(dto.userId())
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé")));

        participation.setGuest(true);
        participation.setFirstName(dto.guest().firstName());
        participation.setEmail(dto.guest().email());
        participation.setPhoneNumber(dto.guest().phoneNumber());
        participationRepository.save(participation);

    }
}
