package com.example.inqooltennisreservationapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
abstract class SoftDeletableEntity {

    @Column(name = "deleted", nullable = false)
    private boolean isDeleted;
}
