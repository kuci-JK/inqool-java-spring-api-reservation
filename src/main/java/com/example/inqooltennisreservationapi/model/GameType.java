package com.example.inqooltennisreservationapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GameType {
    SINGLES(1.0), DOUBLES(1.5);

    private final double priceMultiplier;
}
