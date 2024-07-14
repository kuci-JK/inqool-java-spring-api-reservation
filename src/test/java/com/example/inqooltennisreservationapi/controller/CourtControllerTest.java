package com.example.inqooltennisreservationapi.controller;

import com.example.inqooltennisreservationapi.exceptions.DatabaseException;
import com.example.inqooltennisreservationapi.exceptions.EntityNotFoundException;
import com.example.inqooltennisreservationapi.model.api.CourtDTOs;
import com.example.inqooltennisreservationapi.service.CourtService;
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

@WebMvcTest(CourtController.class)
public class CourtControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CourtService service;

    @Test
    public void list_empty() throws Exception {
        List<CourtDTOs.CourtResponseDTO> expected = List.of();

        given(service.getAllCourts()).willReturn(expected);

        mvc.perform(get("/courts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
        ;
    }

    @Test
    void list_nonEmpty() throws Exception {
        List<CourtDTOs.CourtResponseDTO> expected = List.of(
                getValidCourtResponse(1, "Court", getValidSurfaceResponse(1, "Clay", 10))
        );

        given(service.getAllCourts()).willReturn(expected);

        mvc.perform(get("/courts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(expected.size())))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].courtName").value("Court"))
                .andExpect(jsonPath("$[0].surface.id").value(1))
                .andExpect(jsonPath("$[0].surface.surfaceName").value("Clay"))
                .andExpect(jsonPath("$[0].surface.pricePerMinute").value(10))
                .andExpect(jsonPath("$[0].deleted").value(false));
    }

    @Test
    void list_serviceThrows() throws Exception {
        given(service.getAllCourts()).willThrow(new DatabaseException());

        mvc.perform(get("/courts"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getById_Exists() throws Exception {
        var expected = getValidCourtResponse(1, "Court", getValidSurfaceResponse(1, "Clay", 10));

        given(service.getCourt(1)).willReturn(expected);

        mvc.perform(get("/courts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.courtName").value("Court"))
                .andExpect(jsonPath("$.surface.id").value(1))
                .andExpect(jsonPath("$.surface.surfaceName").value("Clay"))
                .andExpect(jsonPath("$.surface.pricePerMinute").value(10))
                .andExpect(jsonPath("$.deleted").value(false));
    }

    @Test
    void getById_NotExists() throws Exception {
        given(service.getCourt(1)).willThrow(new EntityNotFoundException());

        mvc.perform(get("/courts/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getById_invalidId() throws Exception {
        mvc.perform(get("/courts/invalid_id"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_valid() throws Exception {
        var request = getValidCourtParams("Court", 1);
        var expected = getValidCourtResponse(1, "Court", getValidSurfaceResponse(1, "Clay", 10));

        given(service.createCourt(request)).willReturn(expected);

        mvc.perform(applyHeadersAndContent(post("/courts"), request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.courtName").value("Court"))
                .andExpect(jsonPath("$.surface.id").value(1))
                .andExpect(jsonPath("$.surface.surfaceName").value("Clay"))
                .andExpect(jsonPath("$.surface.pricePerMinute").value(10))
                .andExpect(jsonPath("$.deleted").value(false));
    }

    @Test
    void create_invalidName() throws Exception {
        var request = getValidCourtParams("", 1);


        mvc.perform(applyHeadersAndContent(post("/courts"), request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_invalidSurfaceId_negative() throws Exception {
        var request = getValidCourtParams("Court", -10);

        mvc.perform(applyHeadersAndContent(post("/courts"), request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_invalidSurfaceId_zero() throws Exception {
        var request = getValidCourtParams("Court", 0);

        mvc.perform(applyHeadersAndContent(post("/courts"), request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_invalidRequestBody() throws Exception {
        var request = "{\"hello\":\"world\"}";

        mvc.perform(applyHeaders(post("/courts")).content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_valid() throws Exception {

        var request = getValidCourtParams("Court", 1);
        var expected = getValidCourtResponse(1, "Court", getValidSurfaceResponse(1, "Clay", 10));

        given(service.editCourt(1, request)).willReturn(expected);

        mvc.perform(applyHeadersAndContent(put("/courts/1"), request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.courtName").value("Court"))
                .andExpect(jsonPath("$.surface.id").value(1))
                .andExpect(jsonPath("$.surface.surfaceName").value("Clay"))
                .andExpect(jsonPath("$.surface.pricePerMinute").value(10))
                .andExpect(jsonPath("$.deleted").value(false));
    }

    @Test
    void update_invalidIdParam() throws Exception {
        var request = getValidCourtParams("Court", 1);

        mvc.perform(applyHeadersAndContent(put("/courts/invalid_id"), request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_DoesNotExist() throws Exception {
        var request = getValidCourtParams("Court", 1);

        given(service.editCourt(1, request)).willThrow(new EntityNotFoundException());

        mvc.perform(applyHeadersAndContent(put("/courts/1"), request))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_invalidName() throws Exception {
        var request = getValidCourtParams("", 1);

        mvc.perform(applyHeadersAndContent(put("/courts/1"), request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_invalidSurfaceId_negative() throws Exception {
        var request = getValidCourtParams("Court", -1);

        mvc.perform(applyHeadersAndContent(put("/courts/1"), request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_invalidSurfaceId_zero() throws Exception {
        var request = getValidCourtParams("Court", 0);

        mvc.perform(applyHeadersAndContent(put("/courts/1"), request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_invalidRequestBody() throws Exception {
        var request = "{\"hello\":\"world\"}";

        mvc.perform(applyHeaders(put("/courts/1")).content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_valid() throws Exception {
        var expected = getValidCourtResponse(1, "Court", getValidSurfaceResponse(1, "Clay", 10));

        expected.setDeleted(true);

        given(service.deleteCourt(1)).willReturn(expected);

        mvc.perform(delete("/courts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.courtName").value("Court"))
                .andExpect(jsonPath("$.surface.id").value(1))
                .andExpect(jsonPath("$.surface.surfaceName").value("Clay"))
                .andExpect(jsonPath("$.surface.pricePerMinute").value(10))
                .andExpect(jsonPath("$.deleted").value(true));
    }

    @Test
    void delete_invalidId() throws Exception {
        mvc.perform(delete("/courts/invalid_id"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_nonExistentEntity() throws Exception {
        given(service.deleteCourt(1)).willThrow(new EntityNotFoundException());

        mvc.perform(delete("/courts/1"))
                .andExpect(status().isNotFound());
    }
}
