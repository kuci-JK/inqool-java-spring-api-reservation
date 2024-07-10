package com.example.inqooltennisreservationapi.model.business;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class CourtSurface extends SoftDeletable {

    private long id;

    private String surfaceName;

    private double pricePerMinute;
}
