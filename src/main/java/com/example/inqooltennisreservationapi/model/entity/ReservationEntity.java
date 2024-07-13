package com.example.inqooltennisreservationapi.model.entity;

import com.example.inqooltennisreservationapi.model.GameType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "court_id", nullable = false)
    @Cascade(CascadeType.ALL)
    private CourtEntity reservedCourtEntity;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Cascade(CascadeType.ALL)
    private UserEntity userEntity;

    public double getTotalPrice() {
        var minutesTotal = ChronoUnit.MINUTES.between(reservationStart, reservationEnd);
        return reservedCourtEntity.getSurface().getPricePerMinute() * minutesTotal * gameType.getPriceMultiplier();
    }
}
