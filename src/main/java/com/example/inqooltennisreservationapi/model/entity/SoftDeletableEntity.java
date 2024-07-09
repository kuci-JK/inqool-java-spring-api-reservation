package com.example.inqooltennisreservationapi.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class SoftDeletableEntity {

    @Column(name = "deleted", nullable = false)
    private boolean isDeleted;
}
