package com.example.inqooltennisreservationapi.controller;

import com.example.inqooltennisreservationapi.exceptions.DatabaseException;
import com.example.inqooltennisreservationapi.exceptions.EntityNotFoundException;
import com.example.inqooltennisreservationapi.model.api.CourtSurfaceDTOs;
import com.example.inqooltennisreservationapi.service.CourtSurfaceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.example.inqooltennisreservationapi.TestUtil.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SurfaceController.class)
public class SurfaceControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CourtSurfaceService service;

    @Test
    public void list_empty() throws Exception {
        List<CourtSurfaceDTOs.CourtSurfaceResponseDTO> surfaces = List.of();

        given(service.getAllSurfaces()).willReturn(surfaces);

        mvc.perform(get("/surfaces"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
        ;
    }

    @Test
    void list_nonEmpty() throws Exception {
        List<CourtSurfaceDTOs.CourtSurfaceResponseDTO> surfaces = List.of(
                new CourtSurfaceDTOs.CourtSurfaceResponseDTO(1, "Clay", 10, false)
        );

        given(service.getAllSurfaces()).willReturn(surfaces);

        mvc.perform(get("/surfaces"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(surfaces.size())))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].surfaceName").value("Clay"))
                .andExpect(jsonPath("$[0].pricePerMinute").value(10))
                .andExpect(jsonPath("$[0].deleted").value(false));
    }

    @Test
    void list_serviceThrows() throws Exception {
        given(service.getAllSurfaces()).willThrow(new DatabaseException());

        mvc.perform(get("/surfaces"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getSurfaceById_surfaceExists() throws Exception {
        var surface = getValidSurfaceResponse(1, "Clay", 10);

        given(service.getSurface(1)).willReturn(surface);

        mvc.perform(get("/surfaces/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.surfaceName").value("Clay"))
                .andExpect(jsonPath("$.pricePerMinute").value(10))
                .andExpect(jsonPath("$.deleted").value(false));
    }

    @Test
    void getSurfaceById_surfaceNotExists() throws Exception {
        given(service.getSurface(1)).willThrow(new EntityNotFoundException());

        mvc.perform(get("/surfaces/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getSurfaceById_invalidId() throws Exception {
        mvc.perform(get("/surfaces/invalid_id"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createSurface_valid() throws Exception {

        var request = getValidSurfaceParams("Clay", 10);

        given(service.createSurface(request)).willReturn(getValidSurfaceResponse(1, "Clay", 10));

        mvc.perform(applyHeadersAndContent(post("/surfaces"), request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.surfaceName").value("Clay"))
                .andExpect(jsonPath("$.pricePerMinute").value(10))
                .andExpect(jsonPath("$.deleted").value(false));
    }

    @Test
    void createSurface_invalidName() throws Exception {
        var request = getValidSurfaceParams("", 10);

        mvc.perform(applyHeadersAndContent(post("/surfaces"), request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createSurface_invalidPrice_negative() throws Exception {
        var request = getValidSurfaceParams("Clay", -10);

        mvc.perform(applyHeadersAndContent(post("/surfaces"), request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createSurface_invalidPrice_zero() throws Exception {
        var request = getValidSurfaceParams("", 0);

        mvc.perform(applyHeadersAndContent(post("/surfaces"), request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createSurface_invalidRequestBody() throws Exception {
        var request = "{\"hello\":\"world\"}";

        mvc.perform(applyHeaders(post("/surfaces")).content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateSurface_valid() throws Exception {

        var request = getValidSurfaceParams("Clay", 10);

        given(service.editSurface(1, request)).willReturn(getValidSurfaceResponse(1, "Clay", 10));

        mvc.perform(applyHeadersAndContent(put("/surfaces/1"), request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.surfaceName").value("Clay"))
                .andExpect(jsonPath("$.pricePerMinute").value(10))
                .andExpect(jsonPath("$.deleted").value(false));
    }

    @Test
    void updateSurface_invalidId() throws Exception {
        var request = getValidSurfaceParams("Clay", 10);

        mvc.perform(applyHeadersAndContent(put("/surfaces/invalid_id"), request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateSurface_surfaceDoesNotExist() throws Exception {
        var request = getValidSurfaceParams("Clay", 10);

        given(service.editSurface(1, request)).willThrow(new EntityNotFoundException());

        mvc.perform(applyHeadersAndContent(put("/surfaces/1"), request))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateSurface_invalidName() throws Exception {
        var request = getValidSurfaceParams("", 10);

        mvc.perform(applyHeadersAndContent(put("/surfaces/1"), request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateSurface_invalidPrice_negative() throws Exception {
        var request = getValidSurfaceParams("Clay", -10);

        mvc.perform(applyHeadersAndContent(put("/surfaces/1"), request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateSurface_invalidPrice_zero() throws Exception {
        var request = getValidSurfaceParams("", 0);

        mvc.perform(applyHeadersAndContent(put("/surfaces/1"), request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateSurface_invalidRequestBody() throws Exception {
        var request = "{\"hello\":\"world\"}";

        mvc.perform(applyHeaders(put("/surfaces/1")).content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteSurface_valid() throws Exception {
        var expectedResp = getValidSurfaceResponse(1, "Clay", 10);
        expectedResp.setDeleted(true);

        given(service.deleteSurface(1)).willReturn(expectedResp);

        mvc.perform(delete("/surfaces/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.surfaceName").value("Clay"))
                .andExpect(jsonPath("$.pricePerMinute").value(10))
                .andExpect(jsonPath("$.deleted").value(true));
    }

    @Test
    void deleteSurface_invalidId() throws Exception {
        mvc.perform(delete("/surfaces/invalid_id"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteSurface_nonExistentEntity() throws Exception {
        given(service.deleteSurface(1)).willThrow(new EntityNotFoundException());

        mvc.perform(delete("/surfaces/1"))
                .andExpect(status().isNotFound());
    }
}
