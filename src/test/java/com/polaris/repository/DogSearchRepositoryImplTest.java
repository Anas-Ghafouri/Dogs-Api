package com.polaris.repository;

import com.polaris.model.dto.DogFilter;
import com.polaris.model.dto.DogQueryField;
import com.polaris.model.entity.Dog;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class DogSearchRepositoryImplTest {

    @Inject
    DogSearchRepository dogSearchRepository;

    @Inject
    EntityManager entityManager;

    @Test
    void search_excludesDeletedDogsWhenIncludeDeletedIsFalse() {
        persistDog("Buddy", "Labrador", false);
        persistDog("buddy boy", "Beagle", false);
        persistDog("Max", "Labrador", true);

        entityManager.flush();
        entityManager.clear();

        Page<Dog> result = dogSearchRepository.search(
                null,
                false,
                Pageable.from(0, 10)
        );

        assertEquals(2, result.getContent().size());
        assertEquals(2, result.getTotalSize());
        assertTrue(result.getContent().stream().noneMatch(Dog::isDeleted));
    }

    @Test
    void search_appliesCaseInsensitiveNameFilterAndCountMatchesFilteredRows() {
        persistDog("Ruddy", "Labrador", false);
        persistDog("ruddy boy", "Beagle", false);
        persistDog("Max", "Labrador", false);
        persistDog("Ruddy Deleted", "Poodle", true);

        entityManager.flush();
        entityManager.clear();

        DogFilter filter = new DogFilter(Map.of(DogQueryField.NAME, "RUDDY"));

        Page<Dog> result = dogSearchRepository.search(
                filter,
                false,
                Pageable.from(0, 10)
        );

        assertEquals(2, result.getContent().size());
        assertEquals(2, result.getTotalSize());
        assertTrue(result.getContent().stream()
                .allMatch(dog -> dog.getName().toLowerCase().contains("ruddy")));
        assertTrue(result.getContent().stream().noneMatch(Dog::isDeleted));
    }

    private void persistDog(String name, String breed, boolean deleted) {
        Dog dog = new Dog();
        dog.setName(name);
        dog.setBreed(breed);
        dog.setDeleted(deleted);
        entityManager.persist(dog);
    }
}