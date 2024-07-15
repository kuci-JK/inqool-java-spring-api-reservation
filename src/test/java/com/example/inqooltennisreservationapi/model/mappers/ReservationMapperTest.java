package com.example.inqooltennisreservationapi.model.mappers;

import com.example.inqooltennisreservationapi.exceptions.EntityNotFoundException;
import com.example.inqooltennisreservationapi.exceptions.InvalidRequestException;
import com.example.inqooltennisreservationapi.model.GameType;
import com.example.inqooltennisreservationapi.model.api.CourtDTOs;
import com.example.inqooltennisreservationapi.model.api.CourtSurfaceDTOs;
import com.example.inqooltennisreservationapi.model.api.ReservationDTOs;
import com.example.inqooltennisreservationapi.model.api.UserDTOs;
import com.example.inqooltennisreservationapi.model.entity.CourtEntity;
import com.example.inqooltennisreservationapi.model.entity.CourtSurfaceEntity;
import com.example.inqooltennisreservationapi.model.entity.ReservationEntity;
import com.example.inqooltennisreservationapi.model.entity.UserEntity;
import com.example.inqooltennisreservationapi.repository.CourtRepository;
import com.example.inqooltennisreservationapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class ReservationMapperTest {

    @MockBean
    CourtMapper courtMapper;

    @MockBean
    CourtRepository courtRepository;

    @MockBean
    UserMapper userMapper;

    @MockBean
    UserRepository userRepository;

    @Autowired
    ReservationMapper mapper;

    private static ReservationEntity getExpectedReservationEntity() {
        var surfaceEntity = new CourtSurfaceEntity(1, "surface", 15.0);
        var court = new CourtEntity(1, "name", surfaceEntity);
        var user = new UserEntity(1, "user", "777777777");

        return new ReservationEntity(
                1,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(1),
                LocalDateTime.now().plusHours(1),
                GameType.DOUBLES,
                court,
                user
        );
    }

    private static ReservationDTOs.ReservationResponseDTO getExpectedReservationResponse(ReservationEntity e) {
        var expectedUser = new UserDTOs.UserResponseDTO(
                e.getUserEntity().getId(), e.getUserEntity().getName(),
                e.getUserEntity().getPhone(), e.getUserEntity().isDeleted());
        var expectedSurface = new CourtSurfaceDTOs.CourtSurfaceResponseDTO(
                e.getReservedCourtEntity().getSurface().getId(), e.getReservedCourtEntity().getSurface().getSurfaceName(),
                e.getReservedCourtEntity().getSurface().getPricePerMinute(), e.getReservedCourtEntity().getSurface().isDeleted());
        var expectedCourt = new CourtDTOs.CourtResponseDTO(
                e.getReservedCourtEntity().getId(), e.getReservedCourtEntity().getName(), expectedSurface,
                e.getReservedCourtEntity().isDeleted());

        return new ReservationDTOs.ReservationResponseDTO(
                1,
                e.getCreatedDate(),
                e.getReservationStart(),
                e.getReservationEnd(),
                GameType.DOUBLES,
                e.getTotalPrice(),
                expectedCourt,
                expectedUser,
                true
        );
    }

    @Test
    void entityToResponse() {
        var entity = getExpectedReservationEntity();
        entity.setDeleted(true);

        var expected = getExpectedReservationResponse(entity);

        given(courtMapper.entityToResponseDto(entity.getReservedCourtEntity())).willReturn(expected.getCourt());
        given(userMapper.entityToResponseDto(entity.getUserEntity())).willReturn(expected.getCustomer());

        var actual = mapper.entityToResponseDto(entity);

        assertEquals(expected, actual);
    }

    @Test
    void dtoToEntity() {
        var expected = getExpectedReservationEntity();
        expected.setId(0);

        var dto = new ReservationDTOs.ReservationModifyParams(
                expected.getReservationStart(), expected.getReservationEnd(), expected.getGameType(),
                expected.getReservedCourtEntity().getId(),
                new UserDTOs.UserModifyParams(expected.getUserEntity().getName(), expected.getUserEntity().getPhone())
        );

        given(courtRepository.getCourtById(dto.getCourtId())).willReturn(Optional.of(expected.getReservedCourtEntity()));
        given(userRepository.getUserByPhone(dto.getCustomer().getPhone())).willReturn(Optional.of(expected.getUserEntity()));

        var actual = mapper.dtoToEntity(dto);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getReservationStart(), actual.getReservationStart());
        assertEquals(expected.getReservationEnd(), actual.getReservationEnd());
        assertEquals(expected.getGameType(), actual.getGameType());
        assertEquals(expected.getReservedCourtEntity(), actual.getReservedCourtEntity());
        assertEquals(expected.getUserEntity(), actual.getUserEntity());
    }

    @Test
    void dtoToEntity_courtNotFound_throws() {
        var expected = getExpectedReservationEntity();
        expected.setId(0);

        var dto = new ReservationDTOs.ReservationModifyParams(
                expected.getReservationStart(), expected.getReservationEnd(), expected.getGameType(),
                expected.getReservedCourtEntity().getId(),
                new UserDTOs.UserModifyParams(expected.getUserEntity().getName(), expected.getUserEntity().getPhone())
        );

        given(courtRepository.getCourtById(dto.getCourtId())).willReturn(Optional.empty());
        given(userRepository.getUserByPhone(dto.getCustomer().getPhone())).willReturn(Optional.of(expected.getUserEntity()));

        try {
            mapper.dtoToEntity(dto);
            fail();
        } catch (EntityNotFoundException e) {
            // pass
        }
    }

    @Test
    void dtoToEntity_userNotFound_doesNotThrow() {
        var expected = getExpectedReservationEntity();
        expected.setId(0);
        expected.getUserEntity().setId(0);

        var dto = new ReservationDTOs.ReservationModifyParams(
                expected.getReservationStart(), expected.getReservationEnd(), expected.getGameType(),
                expected.getReservedCourtEntity().getId(),
                new UserDTOs.UserModifyParams(expected.getUserEntity().getName(), expected.getUserEntity().getPhone())
        );

        given(courtRepository.getCourtById(dto.getCourtId())).willReturn(Optional.of(expected.getReservedCourtEntity()));
        given(userRepository.getUserByPhone(dto.getCustomer().getPhone())).willReturn(Optional.empty());


        var actual = mapper.dtoToEntity(dto);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getReservationStart(), actual.getReservationStart());
        assertEquals(expected.getReservationEnd(), actual.getReservationEnd());
        assertEquals(expected.getGameType(), actual.getGameType());
        assertEquals(expected.getReservedCourtEntity(), actual.getReservedCourtEntity());
        assertEquals(expected.getUserEntity(), actual.getUserEntity());
    }

    @Test
    void dtoToEntity_userInvalidName_throws() {
        var expected = getExpectedReservationEntity();
        expected.setId(0);

        var dto = new ReservationDTOs.ReservationModifyParams(
                expected.getReservationStart(), expected.getReservationEnd(), expected.getGameType(),
                expected.getReservedCourtEntity().getId(),
                new UserDTOs.UserModifyParams(expected.getUserEntity().getName(), expected.getUserEntity().getPhone())
        );

        var user = new UserEntity(expected.getUserEntity().getId(), "this is name", expected.getUserEntity().getPhone());

        given(courtRepository.getCourtById(dto.getCourtId())).willReturn(Optional.of(expected.getReservedCourtEntity()));
        given(userRepository.getUserByPhone(dto.getCustomer().getPhone())).willReturn(Optional.of(user));

        try {
            mapper.dtoToEntity(dto);
            fail();
        } catch (InvalidRequestException e) {
            // pass
        }
    }
}
