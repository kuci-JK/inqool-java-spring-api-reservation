package com.example.inqooltennisreservationapi.model.mappers;

import com.example.inqooltennisreservationapi.exceptions.EntityNotFoundException;
import com.example.inqooltennisreservationapi.model.entity.CourtEntity;
import com.example.inqooltennisreservationapi.model.entity.CourtSurfaceEntity;
import com.example.inqooltennisreservationapi.repository.CourtSurfaceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static com.example.inqooltennisreservationapi.TestUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class CourtMapperTest {

    @MockBean
    CourtSurfaceRepository surfaceRepo;

    @MockBean
    CourtSurfaceMapper surfaceMapper;

    @Autowired
    CourtMapper mapper;

    @Test
    void entityToResponse() {
        var surfaceEntity = new CourtSurfaceEntity(1, "surface", 15.0);
        surfaceEntity.setDeleted(true);
        var entity = new CourtEntity(1, "name", surfaceEntity);
        entity.setDeleted(true);

        var expectedSurface = getValidSurfaceResponse(1, "surface", 15.0);
        expectedSurface.setDeleted(true);
        var expected = getValidCourtResponse(1, "name", expectedSurface);
        expected.setDeleted(true);

        given(surfaceMapper.entityToResponseDto(surfaceEntity)).willReturn(expectedSurface);

        var actual = mapper.entityToResponseDto(entity);

        assertEquals(expected, actual);
    }

    @Test
    void dtoToEntity() {
        var expectedSurface = new CourtSurfaceEntity(2, "surface", 15.0);
        var expected = new CourtEntity(0, "name", expectedSurface);

        var dto = getValidCourtParams("name", 2);

        given(surfaceRepo.getCourtSurfaceById(2)).willReturn(Optional.of(expectedSurface));

        var actual = mapper.dtoToEntity(dto);

        assertEquals(expected, actual);
    }

    @Test
    void dtoToEntity_surfaceNotFound_throws() {
        var dto = getValidCourtParams("name", 250);

        given(surfaceRepo.getCourtSurfaceById(2)).willReturn(Optional.empty());

        try {
            mapper.dtoToEntity(dto);
            fail();
        } catch (EntityNotFoundException e) {
            // pass
        }
    }

}
