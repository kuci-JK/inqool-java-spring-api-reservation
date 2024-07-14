package com.example.inqooltennisreservationapi.repository;

import com.example.inqooltennisreservationapi.model.GameType;
import com.example.inqooltennisreservationapi.model.api.CourtDTOs;
import com.example.inqooltennisreservationapi.model.api.CourtSurfaceDTOs;
import com.example.inqooltennisreservationapi.model.api.ReservationDTOs;
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
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static com.example.inqooltennisreservationapi.TestUtil.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReservationRepositoryTest {

    private final CourtDTOs.CourtModifyParams params = getValidCourtParams("court", 1);
    private final CourtSurfaceDTOs.CourtSurfaceModifyParams surfaceParams = getValidSurfaceParams("Clay", 15);
    private final ReservationDTOs.ReservationModifyParams reservationParams = getValidReservationParams(GameType.SINGLES);

    @Autowired
    ReservationRepository repo;

    @Autowired
    CourtRepository courtRepo;

    @Autowired
    CourtSurfaceRepository surfaceRepo;

    @PersistenceContext
    EntityManager em;

    private ReservationEntity prepareReservationEntity(CourtEntity court) {
        return new ReservationEntity(
                0,
                LocalDateTime.now(),
                reservationParams.getReservationStart(),
                reservationParams.getReservationEnd(),
                reservationParams.getGameType(),
                court,
                new UserEntity(0, reservationParams.getCustomer().getName(), reservationParams.getCustomer().getPhone())
        );
    }

    @BeforeEach
    @Transactional
    void setUp() {
        var surface = new CourtSurfaceEntity(0, surfaceParams.getSurfaceName(), surfaceParams.getPricePerMinute());
        var court = new CourtEntity(0, params.getCourtName(), surface);
        var reservation = prepareReservationEntity(court);
        em.persist(surface);
        em.persist(court);
        em.persist(reservation);
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
    void create_valid() {
        var user = em.createQuery("from UserEntity where deleted = false", UserEntity.class)
                .getResultStream().findFirst().orElseThrow();
        var courtEntity = em.createQuery("from CourtEntity where deleted = false", CourtEntity.class)
                .getResultStream().findFirst().orElseThrow();
        var reservation = prepareReservationEntity(courtEntity);
        reservation.setUserEntity(user);
        ;

        var res = repo.createReservation(reservation);

        assertTrue(res.isPresent());
        assertNotEquals(0, res.get().getId());
        assertEquals(0, ChronoUnit.SECONDS.between(reservationParams.getReservationStart(), res.get().getReservationStart()));
        assertEquals(0, ChronoUnit.SECONDS.between(reservationParams.getReservationEnd(), res.get().getReservationEnd()));
        assertEquals(reservationParams.getGameType(), res.get().getGameType());
        assertEquals(courtEntity, res.get().getReservedCourtEntity());
        assertEquals(reservationParams.getCustomer().getName(), res.get().getUserEntity().getName());
        assertEquals(reservationParams.getCustomer().getPhone(), res.get().getUserEntity().getPhone());

        var find = repo.getReservationById(res.get().getId());
        assertTrue(find.isPresent());
        assertEquals(res.get(), find.get());
    }

    @Test
    @Transactional
    void update_existing() {
        var user = em.createQuery("from UserEntity where deleted = false", UserEntity.class)
                .getResultStream().findFirst().orElseThrow();
        var courtEntity = em.createQuery("from CourtEntity where deleted = false", CourtEntity.class)
                .getResultStream().findFirst().orElseThrow();
        var reservationEntity = prepareReservationEntity(courtEntity);
        reservationEntity.setUserEntity(user);
        var id = repo.createReservation(reservationEntity).orElseThrow().getId();

        var entity = prepareReservationEntity(courtEntity);
        entity.setUserEntity(user);
        entity.setReservationEnd(reservationEntity.getReservationEnd().plusHours(1));

        var res = repo.updateReservation(id, entity);

        assertTrue(res.isPresent());
        assertNotEquals(0, res.get().getId());
        assertEquals(0, ChronoUnit.SECONDS.between(entity.getReservationStart(), res.get().getReservationStart()));
        assertEquals(0, ChronoUnit.SECONDS.between(entity.getReservationEnd(), res.get().getReservationEnd()));
        assertEquals(entity.getGameType(), res.get().getGameType());
        assertEquals(courtEntity, res.get().getReservedCourtEntity());
        assertEquals(entity.getUserEntity().getName(), res.get().getUserEntity().getName());
        assertEquals(entity.getUserEntity().getPhone(), res.get().getUserEntity().getPhone());

        var find = repo.getReservationById(res.get().getId());
        assertTrue(find.isPresent());
        assertEquals(res.get(), find.get());
    }

    @Test
    @Transactional
    void update_nonExisting() {
        var courtEntity = em.createQuery("from CourtEntity where deleted = false", CourtEntity.class)
                .getResultStream().findFirst().orElseThrow();
        var entity = prepareReservationEntity(courtEntity);

        var res = repo.updateReservation(150, entity);
        assertTrue(res.isEmpty());

        var find = repo.getReservationById(150);
        assertTrue(find.isEmpty());
    }

    @Test
    @Transactional
    void update_deleted_notAllowed() {
        var user = em.createQuery("from UserEntity where deleted = false", UserEntity.class)
                .getResultStream().findFirst().orElseThrow();
        var courtEntity = em.createQuery("from CourtEntity where deleted = false", CourtEntity.class)
                .getResultStream().findFirst().orElseThrow();
        var reservationEntity = prepareReservationEntity(courtEntity);
        reservationEntity.setUserEntity(user);
        var id = repo.createReservation(reservationEntity).orElseThrow().getId();

        var update = prepareReservationEntity(courtEntity);
        update.setId(id);
        update.setGameType(GameType.DOUBLES);

        repo.deleteReservation(id);

        var res = repo.updateReservation(id, update);
        assertTrue(res.isEmpty());

        var find = em.find(ReservationEntity.class, id);
        assertNotNull(find);
        assertEquals(GameType.SINGLES, find.getGameType());
        assertTrue(find.isDeleted());
    }

    @Test
    @Transactional
    void delete_existing() {
        var user = em.createQuery("from UserEntity where deleted = false", UserEntity.class)
                .getResultStream().findFirst().orElseThrow();
        var courtEntity = em.createQuery("from CourtEntity where deleted = false", CourtEntity.class)
                .getResultStream().findFirst().orElseThrow();
        var reservationEntity = prepareReservationEntity(courtEntity);
        reservationEntity.setUserEntity(user);
        var id = repo.createReservation(reservationEntity).orElseThrow().getId();

        repo.deleteReservation(id);

        var find = repo.getReservationById(id);
        assertTrue(find.isEmpty());

        var find2 = em.find(ReservationEntity.class, id);
        assertNotNull(find2);
        assertEquals(reservationEntity.getCreatedDate(), find2.getCreatedDate());
        assertTrue(find2.isDeleted());
    }

    @Test
    @Transactional
    void delete_nonExisting() {
        var res = repo.deleteReservation(150);
        assertTrue(res.isEmpty());
    }

    @Test
    @Transactional
    void delete_deleted_notAllowed() {
        var user = em.createQuery("from UserEntity where deleted = false", UserEntity.class)
                .getResultStream().findFirst().orElseThrow();
        var courtEntity = em.createQuery("from CourtEntity where deleted = false", CourtEntity.class)
                .getResultStream().findFirst().orElseThrow();
        var reservationEntity = prepareReservationEntity(courtEntity);
        reservationEntity.setUserEntity(user);
        var id = repo.createReservation(reservationEntity).orElseThrow().getId();

        var res = repo.deleteReservation(id);
        assertTrue(res.isEmpty());

        var res2 = repo.deleteReservation(id);
        assertTrue(res2.isEmpty());

        var find = em.find(ReservationEntity.class, id);
        assertNotNull(find);
        assertEquals(reservationEntity.getCreatedDate(), find.getCreatedDate());
        assertTrue(find.isDeleted());
    }

    @Test
    @Transactional
    void getById_existing() {
        var user = em.createQuery("from UserEntity where deleted = false", UserEntity.class)
                .getResultStream().findFirst().orElseThrow();
        var courtEntity = em.createQuery("from CourtEntity where deleted = false", CourtEntity.class)
                .getResultStream().findFirst().orElseThrow();
        var reservationEntity = prepareReservationEntity(courtEntity);
        reservationEntity.setUserEntity(user);
        var id = repo.createReservation(reservationEntity).orElseThrow().getId();

        var res = repo.getReservationById(id);
        assertTrue(res.isPresent());
        assertEquals(id, res.get().getId());
        assertEquals(0, ChronoUnit.SECONDS.between(reservationParams.getReservationStart(), res.get().getReservationStart()));
        assertEquals(0, ChronoUnit.SECONDS.between(reservationParams.getReservationEnd(), res.get().getReservationEnd()));
        assertEquals(reservationParams.getGameType(), res.get().getGameType());
        assertEquals(courtEntity, res.get().getReservedCourtEntity());
        assertEquals(reservationParams.getCustomer().getName(), res.get().getUserEntity().getName());
        assertEquals(reservationParams.getCustomer().getPhone(), res.get().getUserEntity().getPhone());
    }

    @Test
    @Transactional
    void getById_nonExisting() {
        var res = repo.getReservationById(150);
        assertTrue(res.isEmpty());
    }

    @Test
    @Transactional
    void listByCourt_doesNotShowDeleted() {
        // clean reservations
        em.createQuery("delete from ReservationEntity").executeUpdate();

        var courtEntity = em.createQuery("from CourtEntity where deleted = false", CourtEntity.class)
                .getResultStream().findFirst().orElseThrow();
        var user = em.createQuery("from UserEntity where deleted = false", UserEntity.class)
                .getResultStream().findFirst().orElseThrow();

        var reservationEntity = prepareReservationEntity(courtEntity);
        reservationEntity.setUserEntity(user);
        var reservationEntity1 = prepareReservationEntity(courtEntity);
        reservationEntity1.setGameType(GameType.DOUBLES);
        reservationEntity1.setUserEntity(user);

        var deleted = repo.createReservation(reservationEntity).orElseThrow();
        repo.deleteReservation(deleted.getId());

        repo.createReservation(reservationEntity1);
        ;

        var res = repo.listReservations(courtEntity.getId(), false);
        assertEquals(1, res.size());
        assertNotEquals(deleted.getId(), res.get(0).getId());
        assertEquals(0, ChronoUnit.SECONDS.between(reservationEntity1.getCreatedDate(), res.get(0).getCreatedDate()));
        assertEquals(reservationEntity1.getGameType(), res.get(0).getGameType());
        assertFalse(res.get(0).isDeleted());
    }

    @Test
    @Transactional
    void listByPhone_doesNotShowDeleted() {
        // clean reservations
        em.createQuery("delete from ReservationEntity").executeUpdate();

        var courtEntity = em.createQuery("from CourtEntity where deleted = false", CourtEntity.class)
                .getResultStream().findFirst().orElseThrow();
        var user = em.createQuery("from UserEntity where deleted = false", UserEntity.class)
                .getResultStream().findFirst().orElseThrow();

        var reservationEntity = prepareReservationEntity(courtEntity);
        reservationEntity.setUserEntity(user);
        var reservationEntity1 = prepareReservationEntity(courtEntity);
        reservationEntity1.setGameType(GameType.DOUBLES);
        reservationEntity1.setUserEntity(user);

        var deleted = repo.createReservation(reservationEntity).orElseThrow();
        repo.deleteReservation(deleted.getId());

        repo.createReservation(reservationEntity1);
        ;

        var res = repo.listReservations(user.getPhone(), false);
        assertEquals(1, res.size());
        assertNotEquals(deleted.getId(), res.get(0).getId());
        assertEquals(0, ChronoUnit.SECONDS.between(reservationEntity1.getCreatedDate(), res.get(0).getCreatedDate()));
        assertEquals(reservationEntity1.getGameType(), res.get(0).getGameType());
        assertFalse(res.get(0).isDeleted());
    }

    @Test
    @Transactional
    void overlapSelf_noIgnore() {
        var reservation = em.createQuery("from ReservationEntity where deleted = false", ReservationEntity.class)
                .getResultStream().findFirst().orElseThrow();

        var res = repo.overlapsExistingReservations(
                reservation.getReservedCourtEntity().getId(),
                reservation.getReservationStart(),
                reservation.getReservationEnd(),
                Optional.empty()
        );
        assertTrue(res);
    }

    @Test
    @Transactional
    void overlapSelf_ignoreSelf() {
        var reservation = em.createQuery("from ReservationEntity where deleted = false", ReservationEntity.class)
                .getResultStream().findFirst().orElseThrow();

        var res = repo.overlapsExistingReservations(
                reservation.getReservedCourtEntity().getId(),
                reservation.getReservationStart(),
                reservation.getReservationEnd(),
                Optional.of(reservation.getId())
        );
        assertFalse(res);
    }


}
