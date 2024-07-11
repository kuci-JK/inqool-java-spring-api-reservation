package com.example.inqooltennisreservationapi.model.mappers;

import com.example.inqooltennisreservationapi.model.api.UserDTOs;
import com.example.inqooltennisreservationapi.model.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTOs.UserResponseDTO entityToResponseDto(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        return new UserDTOs.UserResponseDTO(userEntity.getId(), userEntity.getName(), userEntity.getPhone(), userEntity.isDeleted());
    }


    public UserEntity dtoToEntity(UserDTOs.UserModifyParams userModifyParams) {
        if (userModifyParams == null) {
            return null;
        }
        return new UserEntity(0, userModifyParams.getName(), userModifyParams.getPhone());
    }
}
