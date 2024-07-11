package com.example.inqooltennisreservationapi.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class SoftDeletableEntity {

    @Column(name = "deleted", nullable = false)
    private boolean deleted;
}
