package com.yonni.raquettelover.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.yonni.raquettelover.controller.advice.GlobalExceptionHandler;
import com.yonni.raquettelover.repository.PlaceRepository;
import com.yonni.raquettelover.security.TestSecurityConfig;
import com.yonni.raquettelover.service.PlaceService;

@Import({ TestSecurityConfig.class, GlobalExceptionHandler.class })
@WebMvcTest(PlaceController.class)
@AutoConfigureMockMvc
@EnableGlobalMethodSecurity(prePostEnabled = true)

public class PlaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PlaceService placeService;

    @MockitoBean
    private PlaceRepository placeRepository;   

    // Utilise un utilisateur mocké avec deux rôles (ADMIN et MANAGER)
    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void testCreatePlace_Success() throws Exception {
        String jsonPayload = """
                {
                    "codeLieu": "test",
                    "name": "Place Name",
                    "address": "Some address",
                    "userId": 1
                }
                """;

        mockMvc.perform(post("/place")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Création effectuée avec succès"));

        verify(placeService).createPlace(any());
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void testCreatePlace_Forbidden() throws Exception {
        String jsonPayload = """
                {
                    "codeLieu": "test",
                    "name": "Place Name",
                    "address": "Some address",
                    "userId": 1
                }
                """;

        mockMvc.perform(post("/place")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isForbidden());

        verifyNoInteractions(placeService);
    }

    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void testCreatePlace_ValidationError() throws Exception {
        String jsonPayload = "{}"; // données invalides selon validation du dto

        mockMvc.perform(post("/place")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));

        verifyNoInteractions(placeService);
    }
}
