package com.yonni.raquettelover.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yonni.raquettelover.configuration.ReservationProperties;
import com.yonni.raquettelover.dto.ApiError;
import com.yonni.raquettelover.dto.ApiResponse;
import com.yonni.raquettelover.dto.PlaceInDto;
import com.yonni.raquettelover.dto.PlaceOutDto;
import com.yonni.raquettelover.dto.SlotResponse;
import com.yonni.raquettelover.entity.Court;
import com.yonni.raquettelover.entity.Place;
import com.yonni.raquettelover.entity.Reservation;
import com.yonni.raquettelover.enumeration.CourtType;
import com.yonni.raquettelover.mapper.PlaceMapper;
import com.yonni.raquettelover.repository.CourtRepository;
import com.yonni.raquettelover.repository.PlaceRepository;
import com.yonni.raquettelover.repository.ReservationRepository;
import com.yonni.raquettelover.security.ValidationUtil;
import com.yonni.raquettelover.service.PlaceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/place") // Sans /api/v1, car context-path déjà /api/v1
@RequiredArgsConstructor
// accessible à tlm
// @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
public class PlaceController {

        private final PlaceService placeService;
        private final PlaceRepository placeRepository;
        private final CourtRepository courtRepository;
        private final ReservationRepository reservationRepository;
        private final ReservationProperties reservationProperties;

        @PostMapping
        public ResponseEntity<?> createPlace(@RequestBody @Valid PlaceInDto dto,
                        BindingResult bindingResult) {

                if (bindingResult.hasErrors()) {
                        return ResponseEntity.badRequest()
                                        .body(ApiResponse.error(ValidationUtil.buildValidationError(bindingResult)));
                }
                placeService.createPlace(dto);

                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(ApiResponse.success(null, "Création effectuée avec succès"));
        }

        @PutMapping("/{placeId}")
        public ResponseEntity<?> updatePlace(@PathVariable Long placeId, @RequestBody @Valid PlaceInDto dto,
                        BindingResult bindingResult) {

                if (bindingResult.hasErrors()) {
                        return ResponseEntity.badRequest()
                                        .body(ApiResponse.error(ValidationUtil.buildValidationError(bindingResult)));
                }

                placeService.updatePlace(dto, placeId);

                // retourner 204 No Content pour indiquer succès sans corps
                return ResponseEntity.ok().body(ApiResponse.success(null, "Modifications enregistrées avec succès"));
        }

        // recupère la liste des lieux de l'utilisateur
        // (pour un admin, ce sera tous les lieux)
        @GetMapping
        public ResponseEntity<?> getPlaces() {
                List<PlaceOutDto> places = placeService.getPlaces();
                return ResponseEntity
                                .ok(ApiResponse.success(places, "Liste des lieux récupérée avec succès"));
        }

        @GetMapping("/{placeId}")
        public ResponseEntity<?> findById(@PathVariable Long placeId) {
                Optional<Place> place = placeRepository.findById(placeId);
                return place
                                .map(PlaceMapper::toDto)
                                .map(dto -> ResponseEntity.ok(ApiResponse.success(dto, "Lieu récupéré avec succès")))
                                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                                                .body(ApiResponse.error(
                                                                new ApiError("PLACE_NOT_FOUND", "Lieu non trouvé"))));
        }

        @GetMapping("/{placeId}/slots")
        public ResponseEntity<List<SlotResponse>> getSlots(
                        @PathVariable Long placeId,
                        @RequestParam CourtType type,
                        @RequestParam LocalDate date) {

                List<Court> courts = courtRepository.findByPlaceIdAndType(placeId, type);
                List<Reservation> reservations = reservationRepository.findByCourtInAndDate(courts, date);

                Map<Long, List<Reservation>> reservationsByCourt = reservations.stream()
                                .collect(Collectors.groupingBy(r -> r.getCourt().getId()));

                LocalTime opening = reservationProperties.getOpeningTime();
                LocalTime closing = reservationProperties.getClosingTime();
                int slotDuration = reservationProperties.getSlotDuration();

                // si la date correspond à la date d'aujourd'hui, on n'affiche que les slots à
                // partir du créneau suivant
                LocalDate today = LocalDate.now();
                if (date.isEqual(today)) {
                        LocalTime now = LocalTime.now();
                        if (now.isAfter(closing) || now.plusMinutes(slotDuration).isAfter(closing)) {
                                // si on est déjà passé l'heure de fermeture ou le prochain créneau va après,
                                // pas de slot dispo
                                return ResponseEntity.ok(new ArrayList<>());
                        }
                        if (now.isAfter(opening)) {
                                // on calcule le prochain créneau
                                int minutes = now.getMinute();
                                int mod = minutes % slotDuration;
                                if (mod != 0) {
                                        now = now.plusMinutes(slotDuration - mod);
                                }
                                opening = now;
                        }
                }

                List<SlotResponse> slots = Stream.iterate(opening,
                                t -> !t.plusMinutes(slotDuration).isAfter(closing),
                                t -> t.plusMinutes(slotDuration))
                                .map(start -> {
                                        LocalTime end = start.plusMinutes(slotDuration);

                                        long freeCourts = courts.stream()
                                                        .filter(court -> {
                                                                List<Reservation> resas = reservationsByCourt
                                                                                .getOrDefault(court.getId(),
                                                                                                List.of());
                                                                // retourne les résas qui ne chevauchent pas le
                                                                // créneau
                                                                return resas.stream()
                                                                                .noneMatch(r -> r.getStartTime()
                                                                                                .toLocalTime()
                                                                                                .isBefore(end)
                                                                                                && r.getEndTime()
                                                                                                                .toLocalTime()
                                                                                                                .isAfter(start));
                                                        })
                                                        .count();

                                        return new SlotResponse(type, start, end, (int) freeCourts);
                                })
                                .toList();

                return ResponseEntity.ok(slots);
        }

}
