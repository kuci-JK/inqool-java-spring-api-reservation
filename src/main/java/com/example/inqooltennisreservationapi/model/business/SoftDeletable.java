package com.example.inqooltennisreservationapi.model.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class SoftDeletable {

    private boolean deleted;

}
