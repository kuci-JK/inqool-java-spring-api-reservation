package com.example.inqooltennisreservationapi.repository;

import com.example.inqooltennisreservationapi.model.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends RepositoryForSoftDeletableEntity {

    Optional<UserEntity> getUserByPhone(String phone);

}
