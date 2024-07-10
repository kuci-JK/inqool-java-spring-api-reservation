package com.example.inqooltennisreservationapi.model.business;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class Court extends SoftDeletable {

    private long id;

    @NonNull
    private String name;


    private CourtSurface surface;

}
