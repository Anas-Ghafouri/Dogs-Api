package com.polaris.service;

import com.polaris.model.dto.DogFilter;
import com.polaris.model.dto.DogRequest;
import com.polaris.model.entity.Dog;
import com.polaris.model.mapper.DogMapper;
import com.polaris.repository.DogSearchRepository;
import com.polaris.repository.DogRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
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

    @Transactional
    public Page<Dog> listDogs(Pageable pageable, DogFilter dogFilter, boolean includeDeleted) {
        if ((dogFilter == null || (isBlank(dogFilter.name()) && isBlank(dogFilter.breed())
                && isBlank(dogFilter.supplier()))) && !includeDeleted) {
            return dogRepository.findAllByDeletedFalse(pageable);
        }
        return dogSearchRepository.search(dogFilter, includeDeleted, pageable);
    }

    public Dog getActiveDog(Long id) {
        return dogRepository.findByIdAndDeletedFalse(id).orElseThrow( () ->
                new NoSuchElementException("Dog not found: " + id));
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

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
