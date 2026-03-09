package com.polaris.repository;

import com.polaris.model.entity.Dog;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.PageableRepository;

import java.util.Optional;

@Repository
public interface DogRepository extends PageableRepository<Dog, Long> {
    Optional<Dog> findByIdAndDeletedFalse(Long id);
    Page<Dog> findAllByDeletedFalse(Pageable pageable);
}
