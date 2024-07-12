package com.example.inqooltennisreservationapi.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.jetbrains.annotations.NotNull;

public interface RepositoryForSoftDeletableEntity {

    default <T> Predicate entityNotDeletetPredicate(@NotNull CriteriaBuilder criteriaBuilder, @NotNull Root<T> root) {
        return criteriaBuilder.isFalse(root.get("deleted"));
    }
}
