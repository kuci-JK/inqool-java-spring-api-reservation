package com.example.inqooltennisreservationapi.repository;

import com.example.inqooltennisreservationapi.exceptions.DatabaseException;
import com.example.inqooltennisreservationapi.model.api.CourtSurfaceDTOs;
import com.example.inqooltennisreservationapi.model.entity.CourtEntity;
import com.example.inqooltennisreservationapi.model.entity.CourtSurfaceEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.inqooltennisreservationapi.TestUtil.getValidSurfaceParams;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CourtSurfaceRepositoryTest {

    private final CourtSurfaceDTOs.CourtSurfaceModifyParams params = getValidSurfaceParams("Clay", 10.0);

    @Autowired
    CourtSurfaceRepository repo;

    @PersistenceContext
    EntityManager em;

    @AfterEach
    @Transactional
    void tearDown() {
        em.createQuery("delete from ReservationEntity").executeUpdate();
        em.createQuery("delete from UserEntity ").executeUpdate();
        em.createQuery("delete from CourtEntity").executeUpdate();
        em.createQuery("delete from CourtSurfaceEntity").executeUpdate();
        em.clear();
        em.flush();
    }

    @Test
    @Transactional
    void create() {
        var res = repo.createCourtSurface(new CourtSurfaceEntity(0, "Hard", 15.0));

        assertTrue(res.isPresent());
        assertNotEquals(0, res.get().getId());
        assertEquals("Hard", res.get().getSurfaceName());
        assertEquals(15.0, res.get().getPricePerMinute());

        var find = repo.getCourtSurfaceById(res.get().getId());
        assertTrue(find.isPresent());
        assertEquals(res.get().getId(), find.get().getId());
        assertEquals(res.get().getSurfaceName(), find.get().getSurfaceName());
        assertEquals(res.get().getPricePerMinute(), find.get().getPricePerMinute());
    }

    @Test
    @Transactional
    void update_existing() {
        var id = repo.createCourtSurface(new CourtSurfaceEntity(0, params.getSurfaceName(), params.getPricePerMinute())).orElseThrow().getId();

        var entity = new CourtSurfaceEntity();
        entity.setSurfaceName("Surface");
        entity.setPricePerMinute(100);

        var res = repo.updateCourtSurface(id, entity);

        assertTrue(res.isPresent());
        assertEquals(id, res.get().getId());
        assertEquals("Surface", res.get().getSurfaceName());
        assertEquals(100, res.get().getPricePerMinute());

        var find = repo.getCourtSurfaceById(res.get().getId());
        assertTrue(find.isPresent());
        assertEquals(res.get().getId(), find.get().getId());
        assertEquals(res.get().getSurfaceName(), find.get().getSurfaceName());
        assertEquals(res.get().getPricePerMinute(), find.get().getPricePerMinute());
    }

    @Test
    @Transactional
    void update_nonExisting() {
        var entity = new CourtSurfaceEntity();
        entity.setSurfaceName("Surface");
        entity.setPricePerMinute(100);

        var res = repo.updateCourtSurface(150, entity);
        assertTrue(res.isEmpty());

        var find = repo.getCourtSurfaceById(150);
        assertTrue(find.isEmpty());
    }

    @Test
    @Transactional
    void update_deleted_notAllowed() {
        var id = repo.createCourtSurface(new CourtSurfaceEntity(0, params.getSurfaceName(), params.getPricePerMinute())).orElseThrow().getId();

        repo.deleteCourtSurface(id);

        var res = repo.updateCourtSurface(id, new CourtSurfaceEntity(id, "SURFACE", 15));
        assertTrue(res.isEmpty());
    }

    @Test
    @Transactional
    void delete_existing() {
        var id = repo.createCourtSurface(new CourtSurfaceEntity(0, params.getSurfaceName(), params.getPricePerMinute())).orElseThrow().getId();

        repo.deleteCourtSurface(id);

        var find = repo.getCourtSurfaceById(id);
        assertTrue(find.isEmpty());
    }

    @Test
    @Transactional
    void delete_nonExisting() {
        var res = repo.deleteCourtSurface(150);
        assertTrue(res.isEmpty());
    }

    @Test
    @Transactional
    void delete_deleted_notAllowed() {
        var id = repo.createCourtSurface(new CourtSurfaceEntity(0, params.getSurfaceName(), params.getPricePerMinute())).orElseThrow().getId();

        var res = repo.deleteCourtSurface(id);
        assertTrue(res.isPresent());
        assertTrue(res.get().isDeleted());

        var res2 = repo.deleteCourtSurface(id);
        assertTrue(res2.isEmpty());
    }

    @Test
    @Transactional
    void delete_used_throws() {
        var entity = repo.createCourtSurface(new CourtSurfaceEntity(0, params.getSurfaceName(), params.getPricePerMinute())).orElseThrow();
        em.persist(new CourtEntity(0, "Court", entity));

        try {
            repo.deleteCourtSurface(entity.getId());
            fail();
        } catch (DatabaseException e) {
            assertTrue(e.getMessage().contains("Cannot delete court surface that is in use"));
        }

        var res = repo.getCourtSurfaceById(entity.getId());
        assertTrue(res.isPresent());
        assertEquals(params.getSurfaceName(), res.get().getSurfaceName());
        assertEquals(params.getPricePerMinute(), res.get().getPricePerMinute());
    }

    @Test
    @Transactional
    void getById_existing() {
        var id = repo.createCourtSurface(new CourtSurfaceEntity(0, params.getSurfaceName(), params.getPricePerMinute())).orElseThrow().getId();

        var res = repo.getCourtSurfaceById(id);
        assertTrue(res.isPresent());
        assertEquals(params.getSurfaceName(), res.get().getSurfaceName());
        assertEquals(params.getPricePerMinute(), res.get().getPricePerMinute());
    }

    @Test
    @Transactional
    void getById_nonExisting() {
        var res = repo.getCourtSurfaceById(150);
        assertTrue(res.isEmpty());
    }

    @Test
    @Transactional
    void list_doesNotShowDeleted() {
        var id = repo.createCourtSurface(new CourtSurfaceEntity(0, params.getSurfaceName(), params.getPricePerMinute())).orElseThrow().getId();

        repo.deleteCourtSurface(id);
        repo.createCourtSurface(new CourtSurfaceEntity(0, "list", 123));

        var res = repo.listCourtSurfaces();
        assertEquals(1, res.size());
        assertEquals("list", res.get(0).getSurfaceName());
        assertEquals(123, res.get(0).getPricePerMinute());
    }
}
