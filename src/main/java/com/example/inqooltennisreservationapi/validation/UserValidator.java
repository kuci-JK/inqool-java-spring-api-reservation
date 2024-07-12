package com.example.inqooltennisreservationapi.validation;

import com.example.inqooltennisreservationapi.exceptions.InvalidRequestException;
import com.example.inqooltennisreservationapi.model.api.UserDTOs;
import org.jetbrains.annotations.NotNull;

public interface UserValidator {

    void validateUserDataMatchOrUserNotExists(@NotNull UserDTOs.UserModifyParams params) throws InvalidRequestException;
}
