package com.example.inqooltennisreservationapi.model.mappers;

import com.example.inqooltennisreservationapi.model.api.UserDTOs;
import com.example.inqooltennisreservationapi.model.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    UserMapper mapper;

    @Test
    void entityToResponse_notDeleted() {
        var entity = new UserEntity(1, "name", "phone");
        var expected = new UserDTOs.UserResponseDTO(1, "name", "phone", false);

        var actual = mapper.entityToResponseDto(entity);

        assertEquals(expected, actual);
    }

    @Test
    void entityToResponse_deleted() {
        var entity = new UserEntity(1, "name", "phone");
        entity.setDeleted(true);
        var expected = new UserDTOs.UserResponseDTO(1, "name", "phone", true);

        var actual = mapper.entityToResponseDto(entity);

        assertEquals(expected, actual);
    }

}
