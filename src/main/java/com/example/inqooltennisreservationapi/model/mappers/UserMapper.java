package com.example.inqooltennisreservationapi.model.mappers;

import com.example.inqooltennisreservationapi.model.api.UserDTOs;
import com.example.inqooltennisreservationapi.model.entity.UserEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public @NotNull UserDTOs.UserResponseDTO entityToResponseDto(@NotNull UserEntity userEntity) {
        return new UserDTOs.UserResponseDTO(userEntity.getId(), userEntity.getName(), userEntity.getPhone(), userEntity.isDeleted());
    }
}
