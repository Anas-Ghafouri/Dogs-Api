package com.polaris.service;

import com.polaris.exception.DogNotFoundException;
import com.polaris.model.dto.DogFilter;
import com.polaris.model.dto.DogRequest;
import com.polaris.model.entity.Dog;
import com.polaris.model.mapper.DogMapper;
import com.polaris.repository.DogSearchRepository;
import com.polaris.repository.DogRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.transaction.annotation.ReadOnly;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.time.Instant;
import java.util.NoSuchElementException;

@Singleton
public class DogService {

    private final DogRepository dogRepository;
    private final DogSearchRepository dogSearchRepository;
    private final DogMapper dogMapper;

    public DogService(DogRepository dogRepository, DogSearchRepository dogSearchRepository, DogMapper dogMapper) {
        this.dogRepository = dogRepository;
        this.dogSearchRepository = dogSearchRepository;
        this.dogMapper = dogMapper;
    }

    @Transactional
    public Dog createDog(DogRequest dogRequest) {
        Dog dog = dogMapper.requestToEntity(dogRequest);
        return dogRepository.save(dog);
    }

    @ReadOnly
    public Page<Dog> listDogs(Pageable pageable, DogFilter dogFilter, boolean includeDeleted) {
        boolean hasFilters = dogFilter != null && !dogFilter.hasNoFilters();

        if (!hasFilters) {
            return includeDeleted
                    ? dogRepository.findAll(pageable)
                    : dogRepository.findAllByDeletedFalse(pageable);
        }
        return dogSearchRepository.search(dogFilter, includeDeleted, pageable);
    }

    @ReadOnly
    public Dog getActiveDog(Long id) {
        return dogRepository.findByIdAndDeletedFalse(id).orElseThrow( () ->
                new DogNotFoundException(id));
    }

    @Transactional
    public Dog updateDog(Long id, DogRequest dogRequest) {
        Dog dog = getActiveDog(id);
        dogMapper.updateEntityFromRequest(dogRequest, dog);
        return dogRepository.update(dog);
    }

    @Transactional
    public void deleteDog(Long id) {
        Dog dog = getActiveDog(id);
        dog.setDeleted(true);
        dog.setDeletedAt(Instant.now());
        dogRepository.update(dog);
    }

}
