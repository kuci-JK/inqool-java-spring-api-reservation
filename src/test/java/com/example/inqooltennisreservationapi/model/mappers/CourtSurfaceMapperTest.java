package com.example.inqooltennisreservationapi.model.mappers;

import com.example.inqooltennisreservationapi.model.entity.CourtSurfaceEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.inqooltennisreservationapi.TestUtil.getValidSurfaceParams;
import static com.example.inqooltennisreservationapi.TestUtil.getValidSurfaceResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CourtSurfaceMapperTest {

    @Autowired
    CourtSurfaceMapper mapper;

    @Test
    void entityToResponse() {
        var surfaceEntity = new CourtSurfaceEntity(1, "surface", 15.0);
        surfaceEntity.setDeleted(true);

        var expectedSurface = getValidSurfaceResponse(1, "surface", 15.0);
        expectedSurface.setDeleted(true);

        var actual = mapper.entityToResponseDto(surfaceEntity);

        assertEquals(expectedSurface, actual);
    }

    @Test
    void dtoToEntity() {
        var expectedSurface = new CourtSurfaceEntity(0, "surface", 15.0);

        var dto = getValidSurfaceParams("surface", 15.0);

        var actual = mapper.dtoToEntity(dto);

        assertEquals(expectedSurface, actual);
    }
}
