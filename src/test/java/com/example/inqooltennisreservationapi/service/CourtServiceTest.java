package com.example.inqooltennisreservationapi.service;

import com.example.inqooltennisreservationapi.exceptions.DatabaseException;
import com.example.inqooltennisreservationapi.exceptions.EntityNotFoundException;
import com.example.inqooltennisreservationapi.model.api.CourtDTOs;
import com.example.inqooltennisreservationapi.model.api.CourtSurfaceDTOs;
import com.example.inqooltennisreservationapi.model.entity.CourtEntity;
import com.example.inqooltennisreservationapi.model.entity.CourtSurfaceEntity;
import com.example.inqooltennisreservationapi.repository.CourtRepository;
import com.example.inqooltennisreservationapi.repository.CourtSurfaceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static com.example.inqooltennisreservationapi.TestUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class CourtServiceTest {

    @MockBean
    CourtRepository repo;

    @MockBean
    CourtSurfaceRepository surfaceRepo;

    @Autowired
    CourtService service;

    @Test
    public void list_empty() {
        given(repo.listCourts()).willReturn(List.of());

        var res = service.getAllCourts();

        assertTrue(res.isEmpty());
    }

    @Test
    void list_nonEmpty() {

        given(repo.listCourts()).willReturn(List.of(new CourtEntity(
                1, "Court", new CourtSurfaceEntity(1, "Clay", 10))));

        var res = service.getAllCourts();

        assertEquals(1, res.size());
        assertEquals(
                getValidCourtResponse(1, "Court", getValidSurfaceResponse(1, "Clay", 10)),
                res.get(0)
        );
    }

    @Test
    void getById_exists() {
        var surface = new CourtSurfaceEntity(1, "Clay", 10);
        var expected = new CourtEntity(1, "Court", surface);
        given(repo.getCourtById(1)).willReturn(Optional.of(expected));

        var res = service.getCourt(1);

        assertEquals(getValidCourtResponse(1, "Court", getValidSurfaceResponse(1, "Clay", 10)), res);
    }

    @Test
    void getById_notExists() {
        given(repo.getCourtById(1)).willReturn(Optional.empty());

        try {
            service.getCourt(1);
            fail(); // should not reach here
        } catch (EntityNotFoundException e) {
            // ignore
        }
    }

    @Test
    void create_valid() {
        var request = getValidCourtParams("Court", 1);

        var surface = new CourtSurfaceEntity(1, "Clay", 10);
        var entity = new CourtEntity(0, "Court", surface);

        given(surfaceRepo.getCourtSurfaceById(1)).willReturn(Optional.of(surface));
        given(repo.createCourt(entity)).willReturn(Optional.of(new CourtEntity(1, "Court", surface)));

        var res = service.createCourt(request);
        assertEquals(1, res.getId());
        assertEquals("Court", res.getCourtName());
        assertEquals("Clay", res.getSurface().getSurfaceName());
        assertEquals(10, res.getSurface().getPricePerMinute());
    }

    @Test
    void create_empty() {
        var request = getValidCourtParams("Court", 1);
        var surface = new CourtSurfaceEntity(1, "Clay", 10);
        var entity = new CourtEntity(1, "Court", surface);

        given(surfaceRepo.getCourtSurfaceById(1)).willReturn(Optional.of(surface));
        given(repo.createCourt(entity)).willReturn(Optional.empty());
        try {
            service.createCourt(request);
            fail();
        } catch (DatabaseException e) {
            // pass
        }
    }

    @Test
    void update_valid() {
        var request = getValidCourtParams("Court", 1);
        var surface = new CourtSurfaceEntity(1, "Clay", 10);
        var entity = new CourtEntity(1, "Court", surface);

        given(surfaceRepo.getCourtSurfaceById(1)).willReturn(Optional.of(surface));
        given(repo.getCourtById(1)).willReturn(Optional.of(entity));
        given(repo.updateCourt(1, entity)).willReturn(Optional.of(entity));

        var res = service.editCourt(1, request);
        assertEquals(getValidCourtResponse(1, "Court", getValidSurfaceResponse(1, "Clay", 10)), res);
    }

    @Test
    void update_notFound() {
        var request = getValidCourtParams("Court", 1);

        given(repo.getCourtById(1)).willReturn(Optional.empty());

        try {
            service.editCourt(1, request);
            fail();
        } catch (EntityNotFoundException e) {
            // pass
        }
    }

    @Test
    void update_editFails() {
        var request = getValidCourtParams("Court", 1);
        var surface = new CourtSurfaceEntity(1, "Clay", 10);
        var entity = new CourtEntity(1, "Court", surface);

        given(surfaceRepo.getCourtSurfaceById(1)).willReturn(Optional.of(surface));
        given(repo.getCourtById(1)).willReturn(Optional.of(entity));
        given(repo.updateCourt(1, entity)).willReturn(Optional.empty());

        try {
            service.editCourt(1, request);
            fail();
        } catch (DatabaseException e) {
            // pass
        }
    }

    @Test
    void delete_valid() {
        var surface = new CourtSurfaceEntity(1, "Clay", 10);
        var entity = new CourtEntity(1, "Court", surface);

        given(repo.getCourtById(1)).willReturn(Optional.of(entity));

        entity.setDeleted(true);
        given(repo.deleteCourt(1)).willReturn(Optional.of(entity));

        var res = service.deleteCourt(1);
        assertEquals(
                new CourtDTOs.CourtResponseDTO(
                        1,
                        "Court",
                        new CourtSurfaceDTOs.CourtSurfaceResponseDTO(1, "Clay", 10, false),
                        true
                ),
                res);
    }

    @Test
    void delete_notExists() {

        given(repo.getCourtById(1)).willReturn(Optional.empty());

        try {
            service.deleteCourt(1);
            fail();
        } catch (EntityNotFoundException e) {
            // pass
        }
    }

    @Test
    void delete_deleteFails() {
        var surface = new CourtSurfaceEntity(1, "Clay", 10);
        var entity = new CourtEntity(1, "Court", surface);

        given(repo.getCourtById(1)).willReturn(Optional.of(entity));
        given(repo.deleteCourt(1)).willReturn(Optional.empty());

        try {
            service.deleteCourt(1);
            fail();
        } catch (DatabaseException e) {
            // pass
        }
    }

}
