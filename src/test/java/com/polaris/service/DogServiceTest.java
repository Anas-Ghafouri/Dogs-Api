package com.polaris.service;

import com.polaris.exception.DogNotFoundException;
import com.polaris.model.dto.DogFilter;
import com.polaris.model.dto.DogQueryField;
import com.polaris.model.dto.DogRequest;
import com.polaris.model.entity.Dog;
import com.polaris.model.entity.DogGender;
import com.polaris.model.entity.DogLeavingReason;
import com.polaris.model.entity.DogStatus;
import com.polaris.model.mapper.DogMapper;
import com.polaris.repository.DogRepository;
import com.polaris.repository.DogSearchRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DogServiceTest {

    @Mock
    DogRepository dogRepository;

    @Mock
    DogSearchRepository dogSearchRepository;

    @Mock
    DogMapper dogMapper;

    @InjectMocks
    DogService dogService;

    @Mock
    DogRequest dogRequest;

    Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = Pageable.from(0, 10);
    }

    @Test
    void listDogs_emptyFilterAndNotIncludeDeletedUsesDogRepository() {
        Dog dog1 = new Dog();
        dog1.setName("Bob");
        Dog dog2 = new Dog();
        dog2.setName("Andy");

        Page<Dog> page = Page.of(List.of(dog1, dog2), pageable, 2L);
        DogFilter dogFilter = new DogFilter(Map.of());

        when(dogRepository.findAllByDeletedFalse(pageable)).thenReturn(page);

        Page<Dog> dogs = dogService.listDogs(pageable, dogFilter, false);

        assertSame(page, dogs);
        verify(dogRepository).findAllByDeletedFalse(pageable);
        verifyNoInteractions(dogSearchRepository);

    }

    @Test
    void listDogs_emptyFilterAndIncludeDeletedUsesDogRepository() {
        Dog dog1 = new Dog();
        dog1.setName("Bob");
        Dog dog2 = new Dog();
        dog2.setName("Andy");

        Page<Dog> page = Page.of(List.of(dog1, dog2), pageable, 2L);
        DogFilter dogFilter = new DogFilter(Map.of());

        when(dogRepository.findAll(pageable)).thenReturn(page);

        Page<Dog> dogs = dogService.listDogs(pageable, dogFilter, true);

        assertSame(page, dogs);
        verify(dogRepository).findAll(pageable);
        verifyNoInteractions(dogSearchRepository);

    }

    @Test
    void listDogs_withFilterUsesDogSearchRepository() {
        Map<DogQueryField, String> filters = new EnumMap<>(DogQueryField.class);
        filters.put(DogQueryField.NAME, "Peter");
        filters.put(DogQueryField.BREED, "German Shepherd");
        filters.put(DogQueryField.SUPPLIER, "Charity A");
        DogFilter dogFilter = new DogFilter(filters);

        Dog dog1 = new Dog();
        dog1.setName("Peter");
        dog1.setBreed("German Shepherd");
        dog1.setSupplier("Charity A");

        Page<Dog> page = Page.of(List.of(dog1), pageable, 1L);

        when(dogSearchRepository.search(dogFilter, false, pageable)).thenReturn(page);

        Page<Dog> result = dogService.listDogs(pageable, dogFilter, false);

        assertSame(page, result);
        verify(dogSearchRepository).search(dogFilter, false, pageable);
        verify(dogRepository, never()).findAllByDeletedFalse(any());

    }

    @Test
    void getActiveDog_returnsDog() {
        Dog dog = new Dog();
        dog.setName("Peter");

        when(dogRepository.findByIdAndDeletedFalse(10L)).thenReturn(Optional.of(dog));

        Dog result = dogService.getActiveDog(10L);

        assertSame(dog, result);
        verify(dogRepository).findByIdAndDeletedFalse(10L);
    }
    @Test
    void getActiveDog_throwsExceptionWhenNotFound() {
        when(dogRepository.findByIdAndDeletedFalse(15L)).thenReturn(Optional.empty());

        DogNotFoundException exception = assertThrows(DogNotFoundException.class,
                () -> dogService.getActiveDog(15L));

        assertEquals("Dog not found: 15", exception.getMessage());

    }

    @Test
    void createDog_mapsRequestAndSavesEntity() {
        DogRequest dogRequest = new DogRequest("Lucy", "a breed", "a supplier", "25",
                DogGender.FEMALE, LocalDate.of(2010, 12, 10),
                LocalDate.of(2012, 12, 10), DogStatus.IN_SERVICE,
                LocalDate.of(2020, 12, 10),
                DogLeavingReason.DIED, "loud");

        Dog mappedDog = new Dog();
        mappedDog.setName("Lucy");
        Dog savedDog = new Dog();
        savedDog.setName("Lucy");

        when(dogMapper.requestToEntity(dogRequest)).thenReturn(mappedDog);
        when(dogRepository.save(mappedDog)).thenReturn(savedDog);

        Dog result = dogService.createDog(dogRequest);

        assertSame(savedDog, result);
        verify(dogMapper).requestToEntity(dogRequest);
        verify(dogRepository).save(mappedDog);
        verifyNoInteractions(dogSearchRepository);
    }

    @Test
    void updateDog_updatesExistingDog() {
        DogRequest dogRequest = new DogRequest("Lucy", "a breed", "a supplier", "25",
                DogGender.FEMALE, LocalDate.of(2010, 12, 10),
                LocalDate.of(2012, 12, 10), DogStatus.IN_SERVICE,
                LocalDate.of(2020, 12, 10),
                DogLeavingReason.DIED, "loud");

        Dog existingDog = new Dog();
        existingDog.setName("Adam");
        Dog updatedDog = new Dog();
        updatedDog.setName("Lucy");

        when(dogRepository.findByIdAndDeletedFalse(10L)).thenReturn(Optional.of(existingDog));
        doNothing().when(dogMapper).updateEntityFromRequest(dogRequest, existingDog);
        when(dogRepository.update(existingDog)).thenReturn(updatedDog);

        Dog resultDog = dogService.updateDog(10L, dogRequest);

        assertSame(updatedDog, resultDog);
        verify(dogRepository).findByIdAndDeletedFalse(10L);
        verify(dogMapper).updateEntityFromRequest(dogRequest, existingDog);
        verify(dogRepository).update(existingDog);
        verifyNoInteractions(dogSearchRepository);
    }

    @Test
    void deleteDog_assignsDeletedAttribute() {
        Dog dog = new Dog();
        dog.setName("Silo");
        dog.setDeleted(false);
        dog.setDeletedAt(null);

        when(dogRepository.findByIdAndDeletedFalse(5L)).thenReturn(Optional.of(dog));

        dogService.deleteDog(5L);

        assertTrue(dog.isDeleted());
        assertNotNull(dog.getDeletedAt());
        assertTrue(dog.getDeletedAt().isBefore(Instant.now()));
        verify(dogRepository).update(dog);
        verifyNoInteractions(dogSearchRepository);

    }


}
