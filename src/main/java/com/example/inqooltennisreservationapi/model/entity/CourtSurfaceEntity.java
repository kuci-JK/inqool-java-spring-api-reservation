package com.example.inqooltennisreservationapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(name = "court_surfaces")
public class CourtSurfaceEntity extends SoftDeletableEntity {
    @Id
    @GeneratedValue
    private long id;

    @NonNull
    @Column(nullable = false, name = "surface_name")
    private String surfaceName;

    @Column(nullable = false, name = "price_per_minute")
    private double pricePerMinute;
}
