package com.example.inqooltennisreservationapi.repository;

import com.example.inqooltennisreservationapi.exceptions.DatabaseException;
import com.example.inqooltennisreservationapi.model.GameType;
import com.example.inqooltennisreservationapi.model.api.CourtDTOs;
import com.example.inqooltennisreservationapi.model.api.CourtSurfaceDTOs;
import com.example.inqooltennisreservationapi.model.entity.CourtEntity;
import com.example.inqooltennisreservationapi.model.entity.CourtSurfaceEntity;
import com.example.inqooltennisreservationapi.model.entity.ReservationEntity;
import com.example.inqooltennisreservationapi.model.entity.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static com.example.inqooltennisreservationapi.TestUtil.getValidCourtParams;
import static com.example.inqooltennisreservationapi.TestUtil.getValidSurfaceParams;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CourtRepositoryTest {

    private final CourtDTOs.CourtModifyParams params = getValidCourtParams("court", 1);
    private final CourtSurfaceDTOs.CourtSurfaceModifyParams surfaceParams = getValidSurfaceParams("Clay", 15);

    @Autowired
    CourtRepository repo;

    @Autowired
    CourtSurfaceRepository surfaceRepo;

    @PersistenceContext
    EntityManager em;

    @BeforeEach
    @Transactional
    void setUp() {
        em.persist(new CourtSurfaceEntity(0, surfaceParams.getSurfaceName(), surfaceParams.getPricePerMinute()));
    }

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
        var surfaceEntity = em.createQuery("from CourtSurfaceEntity where deleted = false", CourtSurfaceEntity.class)
                .getResultStream().findFirst().orElseThrow();
        var res = repo.createCourt(new CourtEntity(0, "Court", surfaceEntity));

        assertTrue(res.isPresent());
        assertNotEquals(0, res.get().getId());
        assertEquals("Court", res.get().getName());
        assertEquals(surfaceEntity, res.get().getSurface());

        var find = repo.getCourtById(res.get().getId());
        assertTrue(find.isPresent());
        assertEquals(res.get().getId(), find.get().getId());
        assertEquals(res.get().getName(), find.get().getName());
        assertEquals(res.get().getSurface(), find.get().getSurface());
    }

    @Test
    @Transactional
    void update_existing() {
        var surfaceEntity = em.createQuery("from CourtSurfaceEntity where deleted = false", CourtSurfaceEntity.class)
                .getResultStream().findFirst().orElseThrow();
        var id = repo.createCourt(new CourtEntity(0, "Court", surfaceEntity)).orElseThrow().getId();

        var entity = new CourtEntity(0, "COURT NAME", surfaceEntity);

        var res = repo.updateCourt(id, entity);

        assertTrue(res.isPresent());
        assertEquals(id, res.get().getId());
        assertEquals("COURT NAME", res.get().getName());
        assertEquals(surfaceEntity, res.get().getSurface());

        var find = repo.getCourtById(res.get().getId());
        assertTrue(find.isPresent());
        assertEquals(res.get().getId(), find.get().getId());
        assertEquals("COURT NAME", res.get().getName());
        assertEquals(surfaceEntity, res.get().getSurface());
    }

    @Test
    @Transactional
    void update_nonExisting() {
        var surfaceEntity = em.createQuery("from CourtSurfaceEntity where deleted = false", CourtSurfaceEntity.class)
                .getResultStream().findFirst().orElseThrow();
        var entity = new CourtEntity(0, "COURT NAME", surfaceEntity);

        var res = repo.updateCourt(150, entity);
        assertTrue(res.isEmpty());

        var find = repo.getCourtById(150);
        assertTrue(find.isEmpty());
    }

    @Test
    @Transactional
    void update_deleted_notAllowed() {
        var surfaceEntity = em.createQuery("from CourtSurfaceEntity where deleted = false", CourtSurfaceEntity.class)
                .getResultStream().findFirst().orElseThrow();
        var id = repo.createCourt(new CourtEntity(0, "Court", surfaceEntity)).orElseThrow().getId();

        repo.deleteCourt(id);

        var res = repo.updateCourt(id, new CourtEntity(id, "SURFACE", surfaceEntity));
        assertTrue(res.isEmpty());

        var find = em.find(CourtEntity.class, id);
        assertNotNull(find);
        assertEquals("Court", find.getName());
        assertEquals(surfaceEntity, find.getSurface());
        assertTrue(find.isDeleted());
    }

    @Test
    @Transactional
    void delete_existing() {
        var surfaceEntity = em.createQuery("from CourtSurfaceEntity where deleted = false", CourtSurfaceEntity.class)
                .getResultStream().findFirst().orElseThrow();
        var id = repo.createCourt(new CourtEntity(0, "Court", surfaceEntity)).orElseThrow().getId();

        repo.deleteCourt(id);

        var find = repo.getCourtById(id);
        assertTrue(find.isEmpty());

        var find2 = em.find(CourtEntity.class, id);
        assertNotNull(find2);
        assertEquals("Court", find2.getName());
        assertTrue(find2.isDeleted());
    }

    @Test
    @Transactional
    void delete_nonExisting() {
        var res = repo.deleteCourt(150);
        assertTrue(res.isEmpty());
    }

    @Test
    @Transactional
    void delete_deleted_notAllowed() {
        var surfaceEntity = em.createQuery("from CourtSurfaceEntity where deleted = false", CourtSurfaceEntity.class)
                .getResultStream().findFirst().orElseThrow();
        var id = repo.createCourt(new CourtEntity(0, "Court", surfaceEntity)).orElseThrow().getId();

        var res = repo.deleteCourt(id);
        assertTrue(res.isPresent());
        assertTrue(res.get().isDeleted());

        var res2 = repo.deleteCourt(id);
        assertTrue(res2.isEmpty());

        var find = em.find(CourtEntity.class, id);
        assertNotNull(find);
        assertEquals("Court", find.getName());
        assertTrue(find.isDeleted());
    }

    @Test
    @Transactional
    void delete_hasReservationInFuture_throws() {
        var surfaceEntity = em.createQuery("from CourtSurfaceEntity where deleted = false", CourtSurfaceEntity.class)
                .getResultStream().findFirst().orElseThrow();
        var entity = repo.createCourt(new CourtEntity(0, "Court", surfaceEntity)).orElseThrow();

        em.persist(new ReservationEntity(
                0,
                LocalDateTime.of(2024, 1, 1, 1, 1),
                LocalDateTime.now().plusHours(10),
                LocalDateTime.now().plusHours(11),
                GameType.SINGLES,
                entity,
                new UserEntity(0, "Name", "777777777")
        ));

        try {
            repo.deleteCourt(entity.getId());
            fail();
        } catch (DatabaseException e) {
            assertTrue(e.getMessage().contains("Cannot delete court. Court has non finished reservations"));
        }

        var res = repo.getCourtById(entity.getId());
        assertTrue(res.isPresent());
        assertEquals("Court", res.get().getName());
    }

    @Test
    @Transactional
    void getById_existing() {
        var surfaceEntity = em.createQuery("from CourtSurfaceEntity where deleted = false", CourtSurfaceEntity.class)
                .getResultStream().findFirst().orElseThrow();
        var id = repo.createCourt(new CourtEntity(0, params.getCourtName(), surfaceEntity)).orElseThrow().getId();

        var res = repo.getCourtById(id);
        assertTrue(res.isPresent());
        assertEquals(id, res.get().getId());
        assertEquals(params.getCourtName(), res.get().getName());
        assertEquals(surfaceEntity, res.get().getSurface());
    }

    @Test
    @Transactional
    void getById_nonExisting() {
        var res = repo.getCourtById(150);
        assertTrue(res.isEmpty());
    }

    @Test
    @Transactional
    void list_doesNotShowDeleted() {
        var surfaceEntity = em.createQuery("from CourtSurfaceEntity where deleted = false", CourtSurfaceEntity.class)
                .getResultStream().findFirst().orElseThrow();
        var id = repo.createCourt(new CourtEntity(0, params.getCourtName(), surfaceEntity)).orElseThrow().getId();

        repo.deleteCourt(id);
        repo.createCourt(new CourtEntity(0, params.getCourtName() + "1", surfaceEntity));
        repo.createCourt(new CourtEntity(0, params.getCourtName() + "2", surfaceEntity));

        var res = repo.listCourts();
        assertEquals(2, res.size());
        assertNotEquals(id, res.get(0).getId());
        assertNotEquals(id, res.get(1).getId());
        assertEquals(params.getCourtName() + "1", res.get(0).getName());
        assertEquals(params.getCourtName() + "2", res.get(1).getName());
        assertEquals(surfaceEntity, res.get(0).getSurface());
        assertEquals(surfaceEntity, res.get(1).getSurface());
    }
}
