package com.example.inqooltennisreservationapi.repository;

import com.example.inqooltennisreservationapi.model.entity.SoftDeletableEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.jetbrains.annotations.NotNull;

public interface RepositoryForSoftDeletableEntity {

    default <T> Predicate entityNotDeletetPredicate(@NotNull CriteriaBuilder criteriaBuilder, @NotNull Root<T> root) {
        return criteriaBuilder.isFalse(root.get("deleted"));
    }

    default <T extends SoftDeletableEntity> boolean entityExists(
            @NotNull EntityManager em, Class<T> entityClass, @NotNull Object primaryKey
    ) {
        var entity = em.find(entityClass, primaryKey);
        return entity != null && !entity.isDeleted();
    }
}
