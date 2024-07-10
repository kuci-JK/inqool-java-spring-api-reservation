package com.example.inqooltennisreservationapi.model.business;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class User extends SoftDeletable {

    private long id;

    private String name;

    private String phone;

}
