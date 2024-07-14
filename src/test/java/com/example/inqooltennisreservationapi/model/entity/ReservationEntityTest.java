package com.example.inqooltennisreservationapi.model.entity;

import com.example.inqooltennisreservationapi.model.GameType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ReservationEntityTest {

    private static ReservationEntity getReservationEntity() {
        return new ReservationEntity(
                0,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                GameType.SINGLES,
                new CourtEntity(0, "Court", new CourtSurfaceEntity(0, "Clay", 10)),
                new UserEntity()
        );
    }

    @Test
    void testGetPrice() {
        var entity = getReservationEntity();
        assertEquals(600, entity.getTotalPrice());
    }

    @Test
    void testGetPrice_appliesMultiplier() {
        var entity = getReservationEntity();
        entity.setGameType(GameType.DOUBLES);
        assertEquals(600 * GameType.DOUBLES.getPriceMultiplier(), entity.getTotalPrice());
    }

}
