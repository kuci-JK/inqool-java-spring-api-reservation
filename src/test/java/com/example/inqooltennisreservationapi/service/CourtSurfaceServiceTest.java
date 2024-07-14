package com.example.inqooltennisreservationapi.service;

import com.example.inqooltennisreservationapi.exceptions.DatabaseException;
import com.example.inqooltennisreservationapi.exceptions.EntityNotFoundException;
import com.example.inqooltennisreservationapi.model.api.CourtSurfaceDTOs;
import com.example.inqooltennisreservationapi.model.entity.CourtSurfaceEntity;
import com.example.inqooltennisreservationapi.repository.CourtSurfaceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static com.example.inqooltennisreservationapi.TestUtil.getValidSurfaceParams;
import static com.example.inqooltennisreservationapi.TestUtil.getValidSurfaceResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class CourtSurfaceServiceTest {

    @MockBean
    CourtSurfaceRepository repo;

    @Autowired
    CourtSurfaceService service;

    @Test
    public void list_empty() {
        given(repo.listCourtSurfaces()).willReturn(List.of());

        var res = service.getAllSurfaces();

        assertTrue(res.isEmpty());
    }

    @Test
    void list_nonEmpty() {

        given(repo.listCourtSurfaces()).willReturn(List.of(new CourtSurfaceEntity(1, "Clay", 10)));

        var res = service.getAllSurfaces();

        assertEquals(1, res.size());
        assertEquals(getValidSurfaceResponse(1, "Clay", 10), res.get(0));
    }

    @Test
    void getSurfaceById_surfaceExists() {
        var expected = getValidSurfaceResponse(1, "Clay", 10);
        given(repo.getCourtSurfaceById(1)).willReturn(Optional.of(new CourtSurfaceEntity(1, "Clay", 10)));

        var res = service.getSurface(1);

        assertEquals(expected, res);
    }

    @Test
    void getSurfaceById_surfaceNotExists() {
        given(repo.getCourtSurfaceById(1)).willReturn(Optional.empty());

        try {
            var res = service.getSurface(1);
            fail(); // should not reach here
        } catch (EntityNotFoundException e) {
            // ignore
        }
    }

    @Test
    void createSurface_valid() {
        var request = getValidSurfaceParams("Clay", 10);
        var entity = new CourtSurfaceEntity(0, "Clay", 10);

        given(repo.createCourtSurface(entity)).willReturn(Optional.of(new CourtSurfaceEntity(1, "Clay", 10)));

        var res = service.createSurface(request);
        assertEquals(1, res.getId());
        assertEquals("Clay", res.getSurfaceName());
        assertEquals(10, res.getPricePerMinute());
    }

    @Test
    void createSurface_empty() {
        var request = getValidSurfaceParams("Clay", 10);
        var entity = new CourtSurfaceEntity(0, "Clay", 10);

        given(repo.createCourtSurface(entity)).willReturn(Optional.empty());
        try {
            service.createSurface(request);
            fail();
        } catch (DatabaseException e) {
            // pass
        }
    }

    @Test
    void updateSurface_valid() {

        var request = getValidSurfaceParams("Clay", 10);
        var entity = new CourtSurfaceEntity(1, "Clay", 10);

        given(repo.getCourtSurfaceById(1)).willReturn(Optional.of(entity));
        given(repo.updateCourtSurface(1, entity)).willReturn(Optional.of(entity));

        var res = service.editSurface(1, request);
        assertEquals(getValidSurfaceResponse(1, "Clay", 10), res);
    }

    @Test
    void updateSurface_invalidId() {
        var request = getValidSurfaceParams("Clay", 10);
        var entity = new CourtSurfaceEntity(1, "Clay", 10);

        given(repo.getCourtSurfaceById(1)).willReturn(Optional.of(entity));
        given(repo.updateCourtSurface(1, entity)).willReturn(Optional.empty());

        try {
            service.editSurface(1, request);
            fail();
        } catch (DatabaseException e) {
            // pass
        }
    }

    @Test
    void deleteSurface_valid() {
        var entity = new CourtSurfaceEntity(1, "Clay", 10);

        given(repo.getCourtSurfaceById(1)).willReturn(Optional.of(entity));

        entity.setDeleted(true);
        given(repo.deleteCourtSurface(1)).willReturn(Optional.of(entity));

        var res = service.deleteSurface(1);
        assertEquals(new CourtSurfaceDTOs.CourtSurfaceResponseDTO(1, "Clay", 10, true), res);
    }

    @Test
    void deleteSurface_surfaceNotExists() {

        given(repo.getCourtSurfaceById(1)).willReturn(Optional.empty());

        try {
            service.deleteSurface(1);
            fail();
        } catch (EntityNotFoundException e) {
            // pass
        }
    }

    @Test
    void deleteSurface_deleteFails() throws Exception {
        var request = getValidSurfaceParams("Clay", 10);
        var entity = new CourtSurfaceEntity(1, "Clay", 10);

        given(repo.getCourtSurfaceById(1)).willReturn(Optional.of(entity));
        given(repo.deleteCourtSurface(1)).willReturn(Optional.empty());

        try {
            service.deleteSurface(1);
            fail();
        } catch (DatabaseException e) {
            // pass
        }
    }

}
