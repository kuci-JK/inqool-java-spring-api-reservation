package com.example.inqooltennisreservationapi.validation.impl;

import com.example.inqooltennisreservationapi.exceptions.InvalidRequestException;
import com.example.inqooltennisreservationapi.model.api.UserDTOs;
import com.example.inqooltennisreservationapi.repository.UserRepository;
import com.example.inqooltennisreservationapi.validation.UserValidator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserValidatorImpl implements UserValidator {

    private final UserRepository repo;

    @Autowired
    public UserValidatorImpl(UserRepository userRepository) {
        this.repo = userRepository;
    }

    @Override
    public void validateUserDataMatchOrUserNotExists(@NotNull UserDTOs.UserModifyParams params) throws InvalidRequestException {
        repo.getUserByPhone(params.getPhone()).ifPresent(u -> {
            if (!Objects.equals(u.getName(), params.getName())) {
                throw new InvalidRequestException("User exists, with different name");
            }
        });
    }

}
