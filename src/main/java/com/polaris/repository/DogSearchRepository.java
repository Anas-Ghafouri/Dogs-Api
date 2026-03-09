package com.polaris.repository;

import com.polaris.model.dto.DogFilter;
import com.polaris.model.entity.Dog;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;

public interface DogSearchRepository {
    Page<Dog> search(DogFilter filter, boolean includeDeleted, Pageable pageable);
}
