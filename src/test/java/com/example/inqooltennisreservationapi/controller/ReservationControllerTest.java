package com.example.inqooltennisreservationapi.controller;

import com.example.inqooltennisreservationapi.exceptions.DatabaseException;
import com.example.inqooltennisreservationapi.exceptions.EntityNotFoundException;
import com.example.inqooltennisreservationapi.matchers.LocalDateTimeMatcher;
import com.example.inqooltennisreservationapi.model.GameType;
import com.example.inqooltennisreservationapi.model.api.ReservationDTOs;
import com.example.inqooltennisreservationapi.service.ReservationService;
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

@WebMvcTest(ReservationController.class)
public class ReservationControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private ReservationService service;

    @Test
    void listReservations_missingParams() throws Exception {
        mvc.perform(get("/reservations"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listReservations_bothParams() throws Exception {
        mvc.perform(get("/reservations?courtId=1&phone=+420555555555"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void listForCourt_empty() throws Exception {
        List<ReservationDTOs.ReservationResponseDTO> expected = List.of();

        given(service.getReservationsOnCourt(1, false)).willReturn(expected);

        mvc.perform(get("/reservations?courtId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
        ;
    }

    @Test
    public void listForCourt_idNegative() throws Exception {

        mvc.perform(get("/reservations?courtId=-1"))
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    public void listForUser_phoneEmpty() throws Exception {

        mvc.perform(get("/reservations?phone="))
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    public void listForUser_empty() throws Exception {
        List<ReservationDTOs.ReservationResponseDTO> expected = List.of();

        given(service.getReservationsForUser("+420555555555", false)).willReturn(expected);

        mvc.perform(get("/reservations?phone=+420555555555"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
        ;
    }

    @Test
    void listForCourt_nonEmpty() throws Exception {

        List<ReservationDTOs.ReservationResponseDTO> expected = List.of(
                getValidReservationResponse(GameType.SINGLES)
        );

        given(service.getReservationsOnCourt(1, false)).willReturn(expected);

        mvc.perform(get("/reservations?courtId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(expected.size())))
                .andExpect(jsonPath("$[0].id").value(expected.get(0).getId()))
                .andExpect(jsonPath("$[0].created").value(new LocalDateTimeMatcher(expected.get(0).getCreated())))
                .andExpect(jsonPath("$[0].reservationStart").value(new LocalDateTimeMatcher(expected.get(0).getReservationStart())))
                .andExpect(jsonPath("$[0].reservationEnd").value(new LocalDateTimeMatcher(expected.get(0).getReservationEnd())))
                .andExpect(jsonPath("$[0].gameType").value(expected.get(0).getGameType().name()))
                .andExpect(jsonPath("$[0].price").value(expected.get(0).getPrice()))
                .andExpect(jsonPath("$[0].court").value(expected.get(0).getCourt()))
                .andExpect(jsonPath("$[0].customer").value(expected.get(0).getCustomer()))
                .andExpect(jsonPath("$[0].deleted").value(expected.get(0).isDeleted()));
    }

    @Test
    void list_serviceThrows() throws Exception {
        given(service.getReservationsOnCourt(1, false)).willThrow(new DatabaseException());

        mvc.perform(get("/reservations?courtId=1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getById_entityExists() throws Exception {
        var expected = getValidReservationResponse(GameType.DOUBLES);

        given(service.getReservation(1)).willReturn(expected);

        mvc.perform(get("/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.created").value(new LocalDateTimeMatcher(expected.getCreated())))
                .andExpect(jsonPath("$.reservationStart").value(new LocalDateTimeMatcher(expected.getReservationStart())))
                .andExpect(jsonPath("$.reservationEnd").value(new LocalDateTimeMatcher(expected.getReservationEnd())))
                .andExpect(jsonPath("$.gameType").value(expected.getGameType().name()))
                .andExpect(jsonPath("$.price").value(expected.getPrice()))
                .andExpect(jsonPath("$.court").value(expected.getCourt()))
                .andExpect(jsonPath("$.customer").value(expected.getCustomer()))
                .andExpect(jsonPath("$.deleted").value(expected.isDeleted()));
    }

    @Test
    void getById_NotExists() throws Exception {
        given(service.getReservation(1)).willThrow(new EntityNotFoundException());

        mvc.perform(get("/reservations/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getById_invalidId() throws Exception {
        mvc.perform(get("/reservations/invalid_id"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_valid() throws Exception {
        var request = getValidReservationParams(GameType.SINGLES);
        var expected = getValidReservationResponse(GameType.SINGLES);

        given(service.createReservation(request)).willReturn(expected);

        mvc.perform(applyHeadersAndContent(post("/reservations"), request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.created").value(new LocalDateTimeMatcher(expected.getCreated())))
                .andExpect(jsonPath("$.reservationStart").value(new LocalDateTimeMatcher(expected.getReservationStart())))
                .andExpect(jsonPath("$.reservationEnd").value(new LocalDateTimeMatcher(expected.getReservationEnd())))
                .andExpect(jsonPath("$.gameType").value(expected.getGameType().name()))
                .andExpect(jsonPath("$.price").value(expected.getPrice()))
                .andExpect(jsonPath("$.court").value(expected.getCourt()))
                .andExpect(jsonPath("$.customer").value(expected.getCustomer()))
                .andExpect(jsonPath("$.deleted").value(expected.isDeleted()));
    }

    @Test
    void create_invalidCustomerName() throws Exception {
        var request = getValidReservationParams(GameType.SINGLES);
        request.getCustomer().setName("");

        mvc.perform(applyHeadersAndContent(post("/reservations"), request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_invalidCustomerPhone() throws Exception {
        var request = getValidReservationParams(GameType.SINGLES);
        request.getCustomer().setPhone("phoneNumber");

        mvc.perform(applyHeadersAndContent(post("/reservations"), request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_invalidCourtId_zero() throws Exception {
        var request = getValidReservationParams(GameType.SINGLES);
        request.setCourtId(0);

        mvc.perform(applyHeadersAndContent(post("/reservations"), request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_invalidCourtId_negative() throws Exception {
        var request = getValidReservationParams(GameType.SINGLES);
        request.setCourtId(-10);

        mvc.perform(applyHeadersAndContent(post("/reservations"), request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_invalidRequestBody() throws Exception {
        var request = "{\"hello\":\"world\"}";

        mvc.perform(applyHeaders(post("/reservations")).content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_valid() throws Exception {

        var request = getValidReservationParams(GameType.DOUBLES);
        var expected = getValidReservationResponse(GameType.DOUBLES);

        given(service.editReservation(1, request)).willReturn(expected);

        mvc.perform(applyHeadersAndContent(put("/reservations/1"), request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.created").value(new LocalDateTimeMatcher(expected.getCreated())))
                .andExpect(jsonPath("$.reservationStart").value(new LocalDateTimeMatcher(expected.getReservationStart())))
                .andExpect(jsonPath("$.reservationEnd").value(new LocalDateTimeMatcher(expected.getReservationEnd())))
                .andExpect(jsonPath("$.gameType").value(expected.getGameType().name()))
                .andExpect(jsonPath("$.price").value(expected.getPrice()))
                .andExpect(jsonPath("$.court").value(expected.getCourt()))
                .andExpect(jsonPath("$.customer").value(expected.getCustomer()))
                .andExpect(jsonPath("$.deleted").value(expected.isDeleted()));
    }

    @Test
    void update_invalidIdParam() throws Exception {
        var request = getValidReservationParams(GameType.SINGLES);

        mvc.perform(applyHeadersAndContent(put("/reservations/invalid_id"), request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_DoesNotExist() throws Exception {
        var request = getValidReservationParams(GameType.SINGLES);

        given(service.editReservation(1, request)).willThrow(new EntityNotFoundException("Not found"));

        mvc.perform(applyHeadersAndContent(put("/reservations/1"), request))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_invalidCustomerName() throws Exception {
        var request = getValidReservationParams(GameType.SINGLES);
        request.getCustomer().setName("");

        mvc.perform(applyHeadersAndContent(put("/reservations/1"), request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_invalidCourtId_negative() throws Exception {
        var request = getValidReservationParams(GameType.SINGLES);
        request.setCourtId(-10);

        mvc.perform(applyHeadersAndContent(put("/reservations/1"), request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_invalidCourtId_zero() throws Exception {
        var request = getValidReservationParams(GameType.SINGLES);
        request.setCourtId(0);

        mvc.perform(applyHeadersAndContent(put("/reservations/1"), request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_invalidRequestBody() throws Exception {
        var request = "{\"hello\":\"world\"}";

        mvc.perform(applyHeaders(put("/reservations/1")).content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_valid() throws Exception {
        var expected = getValidReservationResponse(GameType.SINGLES);

        expected.setDeleted(true);

        given(service.deleteReservation(1)).willReturn(expected);

        mvc.perform(delete("/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.created").value(new LocalDateTimeMatcher(expected.getCreated())))
                .andExpect(jsonPath("$.reservationStart").value(new LocalDateTimeMatcher(expected.getReservationStart())))
                .andExpect(jsonPath("$.reservationEnd").value(new LocalDateTimeMatcher(expected.getReservationEnd())))
                .andExpect(jsonPath("$.gameType").value(expected.getGameType().name()))
                .andExpect(jsonPath("$.price").value(expected.getPrice()))
                .andExpect(jsonPath("$.court").value(expected.getCourt()))
                .andExpect(jsonPath("$.customer").value(expected.getCustomer()))
                .andExpect(jsonPath("$.deleted").value(expected.isDeleted()));
    }

    @Test
    void delete_invalidId() throws Exception {
        mvc.perform(delete("/reservations/invalid_id"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_nonExistentEntity() throws Exception {
        given(service.deleteReservation(1)).willThrow(new EntityNotFoundException());

        mvc.perform(delete("/reservations/1"))
                .andExpect(status().isNotFound());
    }
}
