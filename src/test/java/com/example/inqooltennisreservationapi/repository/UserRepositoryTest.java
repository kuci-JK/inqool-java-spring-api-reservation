package com.example.inqooltennisreservationapi.repository;

import com.example.inqooltennisreservationapi.model.api.UserDTOs;
import com.example.inqooltennisreservationapi.model.entity.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserRepositoryTest {

    private final UserDTOs.UserModifyParams testUser = new UserDTOs.UserModifyParams("John Doe", "777777777");
    @Autowired
    UserRepository repo;
    @PersistenceContext
    EntityManager em;

    @BeforeEach
    @Transactional
    void setUp() {
        var user = new UserEntity(0, testUser.getName(), testUser.getPhone());
        em.persist(user);
    }

    @AfterEach
    @Transactional
    void tearDown() {
        em.createQuery("delete from UserEntity ").executeUpdate();
    }

    @Test
    @Transactional
    void getExisting() {
        var user = repo.getUserByPhone("777777777");

        assertTrue(user.isPresent());
        assertEquals(testUser.getName(), user.get().getName());
        assertEquals(testUser.getPhone(), user.get().getPhone());
    }

    @Test
    @Transactional
    void getNonExisting() {
        var user = repo.getUserByPhone("123456789");

        assertTrue(user.isEmpty());
    }

}
