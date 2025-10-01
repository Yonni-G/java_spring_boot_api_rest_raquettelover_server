package com.yonni.raquettelover.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.yonni.raquettelover.dto.CourtInDto;
import com.yonni.raquettelover.entity.Court;
import com.yonni.raquettelover.entity.Place;
import com.yonni.raquettelover.enumeration.CourtType;
import com.yonni.raquettelover.exception.AccessDeniedExceptionCustom;
import com.yonni.raquettelover.repository.CourtRepository;
import com.yonni.raquettelover.repository.PlaceRepository;
import com.yonni.raquettelover.repository.UserPlaceRepository;
import com.yonni.raquettelover.security.CustomUserDetails;
import com.yonni.raquettelover.security.SecurityUtils;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class CourtServiceTest {

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private UserPlaceRepository userPlaceRepository;

    @Mock
    private CourtRepository courtRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private CourtServiceImpl courtService;

    private CustomUserDetails principal;
    private CourtInDto dto;

    @BeforeEach
    public void setup() {
        principal = mock(CustomUserDetails.class);
        dto = mock(CourtInDto.class);
    }

    @Test
    public void testCreateCourt_AdminUserNotManagingPlace_ShouldThrowAccessDeniedException() {
        Long placeId = 1L;
        Long userIdFromDto = 2L;

        // Mock statique de SecurityUtils
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(principal);

            when(placeRepository.findById(placeId)).thenReturn(Optional.of(new Place()));
            when(userService.hasRoleAdmin(principal)).thenReturn(true);
            when(dto.userId()).thenReturn(userIdFromDto);
            when(userPlaceRepository.existsByUserIdAndPlaceId(userIdFromDto, placeId)).thenReturn(false);

            AccessDeniedExceptionCustom ex = assertThrows(AccessDeniedExceptionCustom.class,
                    () -> courtService.createCourt(dto, placeId));

            assertTrue(ex.getMessage().contains("Accès refusé"));
        }
    }

    @Test
    public void testCreateCourt_ManagerUserNotManagingPlace_ShouldThrowAccessDeniedException() {
        Long placeId = 1L;
        Long principalId = 1L;

        // Mock statique de SecurityUtils
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(principal);

            when(placeRepository.findById(placeId)).thenReturn(Optional.of(new Place()));
            when(userService.hasRoleManager(principal)).thenReturn(true);
            when(principal.getId()).thenReturn(principalId);
            when(userPlaceRepository.existsByUserIdAndPlaceId(principalId, placeId)).thenReturn(false);

            AccessDeniedExceptionCustom ex = assertThrows(AccessDeniedExceptionCustom.class,
                    () -> courtService.createCourt(dto, placeId));

            assertTrue(ex.getMessage().contains("pas le lieu dans"));
        }
    }

    @Test
    public void testCreateCourt_ManagerUserSuccess() {
        Long placeId = 1L;
        Long principalId = 1L;

        // Mock statique de SecurityUtils
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(principal);

            Place place = new Place();
            place.setId(placeId); // Ajoute cette ligne
            when(placeRepository.findById(placeId)).thenReturn(Optional.of(place));

            when(userService.hasRoleManager(principal)).thenReturn(true);
            when(principal.getId()).thenReturn(principalId);
            when(userPlaceRepository.existsByUserIdAndPlaceId(principalId, placeId)).thenReturn(true);

            when(dto.name()).thenReturn("New Court Name");
            when(dto.description()).thenReturn("New Description");
            when(dto.type()).thenReturn(CourtType.PADEL);

            courtService.createCourt(dto, placeId);

            verify(courtRepository).save(argThat(courtToSave -> "New Court Name".equals(courtToSave.getName()) &&
                    "New Description".equals(courtToSave.getDescription()) &&
                    CourtType.PADEL.equals(courtToSave.getType()) &&
                    courtToSave.getPlace() != null &&
                    courtToSave.getPlace().getId().equals(placeId)));
        }
    }

    @Test
    public void testCreateCourt_PlaceNotFound_ShouldThrowEntityNotFoundException() {
        Long placeId = 1L;

        // Mock statique de SecurityUtils
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(principal);

            when(placeRepository.findById(placeId)).thenReturn(Optional.empty());

            EntityNotFoundException ex = assertThrows(
                    EntityNotFoundException.class,
                    () -> courtService.createCourt(dto, placeId));

            assertTrue(ex.getMessage().contains("Lieu non"));
        }
    }

    @Test
    public void testUpdateCourt_CourtNotFound() {
        Long courtId = 1L;
        Long placeId = 2L;

        // Mock statique de SecurityUtils
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(principal);

            when(courtRepository.findById(courtId)).thenReturn(Optional.empty());

            EntityNotFoundException ex = assertThrows(
                    EntityNotFoundException.class,
                    () -> courtService.updateCourt(dto, placeId, courtId));

            assertTrue(ex.getMessage().contains("Terrain non"));
        }
    }

    @Test
    public void testUpdateCourt_CourtNotInPlace() {
        Long courtId = 1L;
        Long placeId = 2L;

        // Mock statique de SecurityUtils
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(principal);

            Court court = new Court();
            court.setId(courtId);
            Place place = new Place();
            place.setId(999L); // le même placeId que dans l’appel du test
            court.setPlace(place);

            when(courtRepository.findById(courtId)).thenReturn(Optional.of(court));

            EntityNotFoundException ex = assertThrows(
                    EntityNotFoundException.class,
                    () -> courtService.updateCourt(dto, placeId, courtId));

            assertTrue(ex.getMessage().contains("Terrain non"));
        }
    }

    @Test
    public void testUpdateCourt_AdminUserCanNotUpdatePlaceForNonOwner_ShouldThrowAccessDeniedException() {
        Long courtId = 1L;
        Long userIdFromDto = 2L;
        Long placeId = 3L;

        // Mock statique de SecurityUtils
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(principal);

            Court court = new Court();
            court.setId(courtId);
            Place place = new Place();
            place.setId(3L); // le même placeId que dans l’appel du test
            court.setPlace(place);

            when(courtRepository.findById(courtId)).thenReturn(Optional.of(court));
            when(userService.hasRoleAdmin(principal)).thenReturn(true);
            when(dto.userId()).thenReturn(userIdFromDto);
            when(userPlaceRepository.existsByUserIdAndPlaceId(userIdFromDto, placeId)).thenReturn(false);

            AccessDeniedExceptionCustom ex = assertThrows(AccessDeniedExceptionCustom.class,
                    () -> courtService.updateCourt(dto, placeId, courtId));

            assertTrue(ex.getMessage().contains("utilisateur ne gére pas"));
        }
    }

    @Test
    public void testUpdateCourt_ManagerUserCanNotUpdateCourtIfNonOwner_ShouldThrowAccessDeniedException() {

        Long principalId = 1L;
        Long placeId = 1L;
        Long courtId = 1L;

        // Mock statique de SecurityUtils
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(principal);

            Court court = new Court();
            court.setId(courtId);
            Place place = new Place();
            place.setId(1L); // le même placeId que dans l’appel du test
            court.setPlace(place);

            when(courtRepository.findById(courtId)).thenReturn(Optional.of(court));
            when(userService.hasRoleAdmin(principal)).thenReturn(false);
            when(userService.hasRoleManager(principal)).thenReturn(true);
            when(principal.getId()).thenReturn(principalId);
            when(userPlaceRepository.existsByUserIdAndPlaceId(principalId, placeId)).thenReturn(false);

            AccessDeniedExceptionCustom ex = assertThrows(AccessDeniedExceptionCustom.class,
                    () -> courtService.updateCourt(dto, placeId, courtId));

            assertTrue(ex.getMessage().contains("Vous ne gérez pas"));
        }
    }

    @Test
    public void testUpdateCourt_ManagerSuccess() {

        Long principalId = 1L;
        Long placeId = 1L;
        Long courtId = 1L;

        // Mock statique de SecurityUtils
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(principal);

            Court court = new Court();
            court.setId(courtId);
            Place place = new Place();
            place.setId(1L); // le même placeId que dans l’appel du test
            court.setPlace(place);

            when(courtRepository.findById(courtId)).thenReturn(Optional.of(court));
            when(userService.hasRoleAdmin(principal)).thenReturn(false);
            when(userService.hasRoleManager(principal)).thenReturn(true);
            when(principal.getId()).thenReturn(principalId);
            when(userPlaceRepository.existsByUserIdAndPlaceId(principalId, placeId)).thenReturn(true);

            when(dto.name()).thenReturn("New Court Name");
            when(dto.description()).thenReturn("New Description");
            when(dto.type()).thenReturn(CourtType.PADEL);

            courtService.updateCourt(dto, placeId, courtId);

            verify(courtRepository).save(argThat(courtToSave ->
                courtToSave.getId().equals(courtId) &&
                "New Court Name".equals(courtToSave.getName()) &&
                "New Description".equals(courtToSave.getDescription()) &&
                CourtType.PADEL.equals(courtToSave.getType()) &&
                courtToSave.getPlace() != null &&
                courtToSave.getPlace().getId().equals(placeId)
            ));
        }
    }
}
