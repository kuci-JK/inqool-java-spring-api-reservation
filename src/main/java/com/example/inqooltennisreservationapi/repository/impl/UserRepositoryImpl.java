package com.example.inqooltennisreservationapi.repository.impl;

import com.example.inqooltennisreservationapi.model.entity.UserEntity;
import com.example.inqooltennisreservationapi.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Optional<UserEntity> getUserByPhone(String phone) {
        var builder = entityManager.getCriteriaBuilder();
        var query = builder.createQuery(UserEntity.class);
        var root = query.from(UserEntity.class);
        query.where(builder.equal(root.get("phone"), builder.parameter(String.class, "phone")));
        query.select(root);

        try {
            return Optional.ofNullable(entityManager.createQuery(query).setParameter("phone", phone).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}