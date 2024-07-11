package com.example.inqooltennisreservationapi.service;

import com.example.inqooltennisreservationapi.model.api.UserDTOs;

public interface UserService {

    UserDTOs.UserResponseDTO getUser(long id);

    UserDTOs.UserResponseDTO getUserByPhone(String phone);

    UserDTOs.UserResponseDTO createUser(UserDTOs.UserModifyParams userModifyParams);

    UserDTOs.UserResponseDTO deleteUser(long id);

}
