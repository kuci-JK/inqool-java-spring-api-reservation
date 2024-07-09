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
@Table(name = "users")
public class UserEntity extends SoftDeletableEntity {

    @Id
    @GeneratedValue
    private long id;

    @NonNull
    @Column(nullable = false)
    private String name;

    @NonNull
    @Column(unique = true, nullable = false)
    private String phone;

}
