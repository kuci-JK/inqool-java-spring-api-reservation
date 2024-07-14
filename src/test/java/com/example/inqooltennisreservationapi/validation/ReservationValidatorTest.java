package com.example.inqooltennisreservationapi.validation;

import com.example.inqooltennisreservationapi.exceptions.InvalidRequestException;
import com.example.inqooltennisreservationapi.model.GameType;
import com.example.inqooltennisreservationapi.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static com.example.inqooltennisreservationapi.TestUtil.getValidReservationParams;
import static org.assertj.core.api.Fail.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
public class ReservationValidatorTest {

    @MockBean
    ReservationRepository repo;

    @MockBean
    UserValidator userValidator;

    @Autowired
    ReservationValidator validator;

    @Test
    void reversedDateInterval() {
        var reservation = getValidReservationParams(GameType.SINGLES);
        reservation.setReservationStart(reservation.getReservationEnd().plusHours(1));

        doNothing().when(userValidator).validateUserDataMatchOrUserNotExists(any());
        given(repo.overlapsExistingReservations(
                reservation.getCourtId(), reservation.getReservationStart(), reservation.getReservationEnd(), Optional.empty()))
                .willReturn(false);

        try {
            validator.validateReservationRequest(Optional.empty(), reservation);
            fail("Should throw exception");
        } catch (InvalidRequestException e) {
            // pass
        }
    }

    @Test
    void hasOverlaps() {
        var reservation = getValidReservationParams(GameType.SINGLES);

        doNothing().when(userValidator).validateUserDataMatchOrUserNotExists(any());
        given(repo.overlapsExistingReservations(
                reservation.getCourtId(), reservation.getReservationStart(), reservation.getReservationEnd(), Optional.empty()))
                .willReturn(true);

        try {
            validator.validateReservationRequest(Optional.empty(), reservation);
            fail("Should throw exception");
        } catch (InvalidRequestException e) {
            // pass
        }
    }

    @Test
    void valid() {
        var reservation = getValidReservationParams(GameType.SINGLES);

        doNothing().when(userValidator).validateUserDataMatchOrUserNotExists(any());
        given(repo.overlapsExistingReservations(
                reservation.getCourtId(), reservation.getReservationStart(), reservation.getReservationEnd(), Optional.empty()))
                .willReturn(false);

        try {
            validator.validateReservationRequest(Optional.empty(), reservation);
        } catch (InvalidRequestException e) {
            fail("Should not throw exception");
        }
    }


}
