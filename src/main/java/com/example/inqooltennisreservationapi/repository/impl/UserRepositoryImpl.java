package com.example.inqooltennisreservationapi.repository.impl;

import com.example.inqooltennisreservationapi.model.entity.UserEntity;
import com.example.inqooltennisreservationapi.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<UserEntity> createUser(UserEntity userEntity) {
        entityManager.persist(userEntity);
        return Optional.of(userEntity);
    }

    @Override
    public Optional<UserEntity> deleteUser(long id) {
        var existing = entityManager.find(UserEntity.class, id);
        if (existing == null) {
            return Optional.empty();
        }

        existing.setDeleted(true);
        var res = entityManager.merge(existing);
        return Optional.of(res);
    }

    @Override
    public Optional<UserEntity> getUserById(long id) {
        return Optional.ofNullable(entityManager.find(UserEntity.class, id));
    }

    @Override
    public Optional<UserEntity> getUserByPhone(String phone) {
        var builder = entityManager.getCriteriaBuilder();
        var query = builder.createQuery(UserEntity.class);
        var root = query.from(UserEntity.class);
        query.where(builder.equal(root.get("phone"), phone));
        query.select(root);

        try {
            return Optional.ofNullable(entityManager.createQuery(query).getSingleResult());
        } catch (PersistenceException e) {
            return Optional.empty();
        }
    }
}