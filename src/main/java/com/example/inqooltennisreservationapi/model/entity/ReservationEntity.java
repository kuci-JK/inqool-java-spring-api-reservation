package com.example.inqooltennisreservationapi.model.entity;

import com.example.inqooltennisreservationapi.model.GameType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(name = "reservations")
public class ReservationEntity extends SoftDeletableEntity {

    @Id
    @GeneratedValue
    private long id;

    @NonNull
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @NonNull
    @Column(name = "reservation_start", nullable = false)
    private LocalDateTime reservationStart;

    @NonNull
    @Column(name = "reservation_end", nullable = false)
    private LocalDateTime reservationEnd;

    @NonNull
    @Column(name = "game_type", nullable = false)
    private GameType gameType;

    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "court_id", nullable = false)
    private CourtEntity reservedCourtEntity;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;
}
