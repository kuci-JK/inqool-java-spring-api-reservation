package com.example.inqooltennisreservationapi.service.impl;

import com.example.inqooltennisreservationapi.exceptions.DatabaseException;
import com.example.inqooltennisreservationapi.exceptions.EntityNotFoundException;
import com.example.inqooltennisreservationapi.exceptions.InvalidRequestException;
import com.example.inqooltennisreservationapi.model.api.UserDTOs;
import com.example.inqooltennisreservationapi.model.mappers.UserMapper;
import com.example.inqooltennisreservationapi.repository.UserRepository;
import com.example.inqooltennisreservationapi.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }


    @Override
    public UserDTOs.UserResponseDTO getUser(long id) {
        var res = userRepository.getUserById(id);
        if (res.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }
        return userMapper.entityToResponseDto(res.get());
    }

    @Override
    public UserDTOs.UserResponseDTO getUserByPhone(String phone) {
        var res = userRepository.getUserByPhone(phone);
        if (res.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }
        return userMapper.entityToResponseDto(res.get());
    }

    @Override
    public UserDTOs.UserResponseDTO createUser(UserDTOs.UserModifyParams userModifyParams) {
        if (userExists(userModifyParams.getPhone())) {
            throw new InvalidRequestException("User with given phone already exists");
        }

        var entity = userMapper.dtoToEntity(userModifyParams);
        var res = userRepository.createUser(entity);
        if (res.isEmpty()) {
            throw new DatabaseException("Failed to create user");
        }

        return userMapper.entityToResponseDto(res.get());
    }

    @Override
    public UserDTOs.UserResponseDTO deleteUser(long id) {
        if (!userExists(id)) {
            throw new EntityNotFoundException("User does not exist");
        }
        var res = userRepository.deleteUser(id);
        if (res.isEmpty()) {
            throw new DatabaseException("Failed to delete user");
        }
        return userMapper.entityToResponseDto(res.get());
    }

    private boolean userExists(String phone) {
        return userRepository.getUserByPhone(phone).isPresent();
    }

    private boolean userExists(long id) {
        return userRepository.getUserById(id).isPresent();
    }
}
