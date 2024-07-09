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
@Table(name = "courts")
public class CourtEntity extends SoftDeletableEntity {

    @Id
    @GeneratedValue
    private long id;

    @NonNull
    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "surface_id", nullable = false)
    private CourtSurfaceEntity surface;

}
