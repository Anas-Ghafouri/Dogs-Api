package com.polaris.repository;

import com.polaris.model.entity.Dog;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;

import java.util.Optional;

public interface DogRepository extends JpaRepository<Dog, Long>, DogPaginationRepository {
    Optional<Dog> findByIdAndDeletedFalse(Long id);
    Page<Dog> findAllByDeletedFalse(Pageable pageable);
}
