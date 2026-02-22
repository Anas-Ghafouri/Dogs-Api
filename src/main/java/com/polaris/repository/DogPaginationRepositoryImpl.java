package com.polaris.repository;

import com.polaris.model.dto.DogFilter;
import com.polaris.model.entity.Dog;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

import java.util.ArrayList;

@Singleton
public class DogPaginationRepositoryImpl implements DogPaginationRepository {

    private final EntityManager entityManager;

    public DogPaginationRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public Page<Dog> search(DogFilter filter, boolean includeDeleted, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Dog> criteriaQuery = criteriaBuilder.createQuery(Dog.class);
        Root<Dog> root = criteriaQuery.from(Dog.class);

        var predicates = buildPredicates(filter, includeDeleted, criteriaBuilder, root);
        criteriaQuery.where(predicates.toArray(Predicate[]::new));

        TypedQuery<Dog> entityManagerQuery = entityManager.createQuery(criteriaQuery);
        entityManagerQuery.setFirstResult((int) pageable.getOffset());
        entityManagerQuery.setMaxResults(pageable.getSize());
        var results = entityManagerQuery.getResultList();

        CriteriaQuery<Long> criteriaCountQuery = criteriaBuilder.createQuery(Long.class);
        Root<Dog> countRoot = criteriaCountQuery.from(Dog.class);
        criteriaCountQuery.select(criteriaBuilder.count(countRoot));
        var countPredicates = buildPredicates(filter, includeDeleted, criteriaBuilder, countRoot);
        criteriaCountQuery.where(countPredicates.toArray(Predicate[]::new));
        Long total = entityManager.createQuery(criteriaCountQuery).getSingleResult();

        return Page.of(results, pageable, total);
    }

    private ArrayList<Predicate> buildPredicates(DogFilter filter, boolean includeDeleted, CriteriaBuilder criteriaBuilder,
                                                 Root<Dog> root) {

        var predicatesList = new ArrayList<Predicate>();

        if (!includeDeleted) {
            predicatesList.add((criteriaBuilder.isFalse(root.get("deleted"))));
        }

        if (filter != null) {
            if (containsText(filter.name())) {
                predicatesList.add(dogsLike(criteriaBuilder, root.get("name"), filter.name()));
            }
            if (containsText(filter.breed())) {
                predicatesList.add(dogsLike(criteriaBuilder, root.get("breed"), filter.breed()));
            }
            if (containsText(filter.supplier())) {
                predicatesList.add(dogsLike(criteriaBuilder, root.get("supplier"), filter.supplier()));
            }
        }
        return predicatesList;
    }

    private Predicate dogsLike(CriteriaBuilder criteriaBuilder, Path<String> field, String pattern) {
        return criteriaBuilder.like(criteriaBuilder.lower(field), "%" + pattern.toLowerCase() + "%");
    }

    private boolean containsText(String s) {
        return s != null && !s.trim().isEmpty();
    }
}
