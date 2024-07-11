package com.example.inqooltennisreservationapi.repository;

import com.example.inqooltennisreservationapi.model.entity.UserEntity;

import java.util.Optional;

public interface UserRepository {

    Optional<UserEntity> createUser(UserEntity userEntity);

    Optional<UserEntity> deleteUser(long id);

    Optional<UserEntity> getUserById(long id);

    Optional<UserEntity> getUserByPhone(String phone);

}
