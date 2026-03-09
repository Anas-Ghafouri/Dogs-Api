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
import java.util.List;
import java.util.Locale;

@Singleton
public class DogSearchRepositoryImpl implements DogSearchRepository {

    private final EntityManager entityManager;

    public DogSearchRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public Page<Dog> search(DogFilter filter, boolean includeDeleted, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Dog> mainQuery = criteriaBuilder.createQuery(Dog.class);
        Root<Dog> root = mainQuery.from(Dog.class);
        mainQuery.select(root);

        var predicates = buildPredicates(filter, includeDeleted, criteriaBuilder, root);
        mainQuery.where(predicates.toArray(Predicate[]::new));

        TypedQuery<Dog> typedQuery = entityManager.createQuery(mainQuery);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getSize());
        var results = typedQuery.getResultList();

        // Separate count query required to calculate total results for pagination
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Dog> countRoot = countQuery.from(Dog.class);
        countQuery.select(criteriaBuilder.count(countRoot));
        var countPredicates = buildPredicates(filter, includeDeleted, criteriaBuilder, countRoot);
        countQuery.where(countPredicates.toArray(Predicate[]::new));
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return Page.of(results, pageable, total);
    }

    private Predicate dogsLike(CriteriaBuilder criteriaBuilder, Path<String> field, String pattern) {
        return criteriaBuilder.like(criteriaBuilder.lower(field), "%" + pattern.toLowerCase(Locale.ROOT) + "%");
    }

    private List<Predicate> buildPredicates(DogFilter filter, boolean includeDeleted, CriteriaBuilder criteriaBuilder,
                                            Root<Dog> root) {

        var predicatesList = new ArrayList<Predicate>();

        applyDeleteFilter(includeDeleted, criteriaBuilder, root, predicatesList);
        applyStringFilters(filter, criteriaBuilder, root, predicatesList);

        return predicatesList;
    }

    private void applyDeleteFilter(boolean includeDeleted,
                                   CriteriaBuilder cb,
                                   Root<Dog> root,
                                   List<Predicate> predicates) {
        if (!includeDeleted) {
            predicates.add(cb.isFalse(root.get("deleted")));
        }
    }

    private void applyStringFilters(DogFilter dogFilter,
                                    CriteriaBuilder cb,
                                    Root<Dog> root,
                                    List<Predicate> predicates) {
        if (dogFilter == null || dogFilter.hasNoFilters()) {
            return;
        }

        // Apply dynamic string filters defined in DogFilter
        dogFilter.filters().forEach((key, value) -> {
            if (value == null || value.trim().isEmpty()) {
                return;
            }
            predicates.add(dogsLike(cb, root.get(key.getEntityField()), value));
        });
    }
}
