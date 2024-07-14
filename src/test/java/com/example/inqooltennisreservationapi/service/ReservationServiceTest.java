package com.example.inqooltennisreservationapi.service;

import com.example.inqooltennisreservationapi.exceptions.DatabaseException;
import com.example.inqooltennisreservationapi.exceptions.EntityNotFoundException;
import com.example.inqooltennisreservationapi.exceptions.InvalidRequestException;
import com.example.inqooltennisreservationapi.model.GameType;
import com.example.inqooltennisreservationapi.model.api.ReservationDTOs;
import com.example.inqooltennisreservationapi.model.entity.CourtEntity;
import com.example.inqooltennisreservationapi.model.entity.CourtSurfaceEntity;
import com.example.inqooltennisreservationapi.model.entity.ReservationEntity;
import com.example.inqooltennisreservationapi.model.entity.UserEntity;
import com.example.inqooltennisreservationapi.model.mappers.ReservationMapper;
import com.example.inqooltennisreservationapi.repository.ReservationRepository;
import com.example.inqooltennisreservationapi.validation.ReservationValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static com.example.inqooltennisreservationapi.TestUtil.getValidReservationParams;
import static com.example.inqooltennisreservationapi.TestUtil.getValidReservationResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
public class ReservationServiceTest {

    @MockBean
    ReservationRepository repo;

    @MockBean
    ReservationValidator validator;

    @MockBean
    ReservationMapper mapper;

    @Autowired
    ReservationService service;

    private static ReservationEntity getEntityFromExpectedResponse(ReservationDTOs.ReservationResponseDTO resp) {
        return new ReservationEntity(
                resp.getId(),
                resp.getCreated(),
                resp.getReservationStart(),
                resp.getReservationEnd(),
                resp.getGameType(),
                new CourtEntity(
                        resp.getCourt().getId(),
                        resp.getCourt().getCourtName(),
                        new CourtSurfaceEntity(
                                resp.getCourt().getSurface().getId(),
                                resp.getCourt().getSurface().getSurfaceName(),
                                resp.getCourt().getSurface().getPricePerMinute()
                        )
                ),
                new UserEntity(
                        1,
                        resp.getCustomer().getName(),
                        resp.getCustomer().getPhone()
                )
        );
    }

    @Test
    public void listByCourt_empty() {
        given(repo.listReservations(1, false)).willReturn(List.of());

        var res = service.getReservationsOnCourt(1, false);

        assertTrue(res.isEmpty());
    }

    @Test
    public void listByUser_empty() {
        given(repo.listReservations("777777777", false)).willReturn(List.of());

        var res = service.getReservationsForUser("777777777", false);

        assertTrue(res.isEmpty());
    }

    @Test
    void list_nonEmpty() {
        var expected = getValidReservationResponse(GameType.SINGLES);
        var reservationEntity = getEntityFromExpectedResponse(expected);

        given(mapper.entityToResponseDto(reservationEntity)).willReturn(expected);
        given(repo.listReservations(1, false)).willReturn(List.of(reservationEntity));

        var res = service.getReservationsOnCourt(1, false);

        assertEquals(1, res.size());
        assertEquals(
                expected,
                res.get(0)
        );
    }

    @Test
    void getById_exists() {
        var expected = getValidReservationResponse(GameType.SINGLES);
        var reservationEntity = getEntityFromExpectedResponse(expected);

        given(repo.getReservationById(1)).willReturn(Optional.of(reservationEntity));
        given(mapper.entityToResponseDto(reservationEntity)).willReturn(expected);

        var res = service.getReservation(1);

        assertEquals(expected, res);
    }

    @Test
    void getById_notExists() {
        given(repo.getReservationById(1)).willReturn(Optional.empty());

        try {
            service.getReservation(1);
            fail(); // should not reach here
        } catch (EntityNotFoundException e) {
            // ignore
        }
    }

    @Test
    void create_valid() {
        var request = getValidReservationParams(GameType.SINGLES);
        var expected = getValidReservationResponse(GameType.SINGLES);
        var reservationEntity = getEntityFromExpectedResponse(expected);

        given(mapper.dtoToEntity(request)).willReturn(reservationEntity);
        given(mapper.entityToResponseDto(reservationEntity)).willReturn(expected);
        doNothing().when(validator).validateReservationRequest(Optional.empty(), request);
        given(repo.createReservation(reservationEntity)).willReturn(Optional.of(reservationEntity));

        var res = service.createReservation(request);
        assertEquals(expected, res);
    }

    @Test
    void create_empty() {
        var request = getValidReservationParams(GameType.SINGLES);
        var expected = getValidReservationResponse(GameType.SINGLES);
        var reservationEntity = getEntityFromExpectedResponse(expected);

        given(repo.createReservation(reservationEntity)).willReturn(Optional.empty());
        try {
            service.createReservation(request);
            fail();
        } catch (DatabaseException e) {
            // pass
        }
    }

    @Test
    void update_valid() {
        var request = getValidReservationParams(GameType.SINGLES);
        var expected = getValidReservationResponse(GameType.SINGLES);
        var reservationEntity = getEntityFromExpectedResponse(expected);
        reservationEntity.setId(1);

        given(mapper.dtoToEntity(request)).willReturn(reservationEntity);
        given(mapper.entityToResponseDto(reservationEntity)).willReturn(expected);
        doNothing().when(validator).validateReservationRequest(Optional.empty(), request);

        given(repo.overlapsExistingReservations(1, request.getReservationStart(), request.getReservationEnd(), Optional.of(1L))).willReturn(false);
        given(repo.getReservationById(1)).willReturn(Optional.of(reservationEntity));
        given(repo.updateReservation(1, reservationEntity)).willReturn(Optional.of(reservationEntity));

        var res = service.editReservation(1, request);
        assertEquals(expected, res);
    }

    @Test
    void update_invalid() {
        var request = getValidReservationParams(GameType.SINGLES);
        var expected = getValidReservationResponse(GameType.SINGLES);
        var reservationEntity = getEntityFromExpectedResponse(expected);
        reservationEntity.setId(1);

        given(repo.getReservationById(1)).willReturn(Optional.of(reservationEntity));
        doThrow(new InvalidRequestException()).when(validator).validateReservationRequest(Optional.of(reservationEntity.getId()), request);

        try {
            service.editReservation(1, request);
            fail();
        } catch (InvalidRequestException e) {
            // pass
        }
    }

    @Test
    void update_notFound() {
        var request = getValidReservationParams(GameType.SINGLES);

        given(repo.getReservationById(1)).willReturn(Optional.empty());

        try {
            service.editReservation(1, request);
            fail();
        } catch (EntityNotFoundException e) {
            // pass
        }
    }

    @Test
    void update_editFails() {
        var request = getValidReservationParams(GameType.SINGLES);
        var expected = getValidReservationResponse(GameType.SINGLES);
        var reservationEntity = getEntityFromExpectedResponse(expected);
        reservationEntity.setId(1);

        given(mapper.dtoToEntity(request)).willReturn(reservationEntity);
        doNothing().when(validator).validateReservationRequest(Optional.empty(), request);

        given(repo.overlapsExistingReservations(1, request.getReservationStart(), request.getReservationEnd(), Optional.of(1L))).willReturn(false);
        given(repo.getReservationById(1)).willReturn(Optional.of(reservationEntity));
        given(repo.updateReservation(1, reservationEntity)).willReturn(Optional.empty());

        try {
            service.editReservation(1, request);
            fail();
        } catch (DatabaseException e) {
            // pass
        }
    }

    @Test
    void delete_valid() {
        var request = getValidReservationParams(GameType.SINGLES);
        var expected = getValidReservationResponse(GameType.SINGLES);
        var reservationEntity = getEntityFromExpectedResponse(expected);
        reservationEntity.setId(1);

        given(repo.getReservationById(1)).willReturn(Optional.of(reservationEntity));

        reservationEntity.setDeleted(true);
        expected.setDeleted(true);

        given(repo.deleteReservation(1)).willReturn(Optional.of(reservationEntity));
        given(mapper.entityToResponseDto(reservationEntity)).willReturn(expected);

        var res = service.deleteReservation(1);
        assertEquals(expected, res);
    }

    @Test
    void delete_notExists() {

        given(repo.getReservationById(1)).willReturn(Optional.empty());

        try {
            service.deleteReservation(1);
            fail();
        } catch (EntityNotFoundException e) {
            // pass
        }
    }

    @Test
    void delete_deleteFails() {
        var expected = getValidReservationResponse(GameType.SINGLES);
        var reservationEntity = getEntityFromExpectedResponse(expected);

        given(repo.getReservationById(1)).willReturn(Optional.of(reservationEntity));
        given(repo.deleteReservation(1)).willReturn(Optional.empty());

        try {
            service.deleteReservation(1);
            fail();
        } catch (DatabaseException e) {
            // pass
        }
    }
}
