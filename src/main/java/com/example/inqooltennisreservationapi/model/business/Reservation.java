package com.example.inqooltennisreservationapi.model.business;

import com.example.inqooltennisreservationapi.model.GameType;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class Reservation extends SoftDeletable {

    private long id;

    private LocalDateTime createdDate;

    private LocalDateTime reservationStart;

    private LocalDateTime reservationEnd;

    private GameType gameType;

    private double totalPrice;

    private Court reservedCourt;

    private User user;

}
