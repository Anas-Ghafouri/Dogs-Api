package com.polaris.service;

import com.polaris.model.dto.DogFilter;
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
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

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
    void listDogs_nullFilterAndNotIncludeDeletedUsesDogRepository() {
        Dog dog1 = new Dog();
        dog1.setName("Bob");
        Dog dog2 = new Dog();
        dog2.setName("Andy");

        Page<Dog> page = Page.of(List.of(dog1, dog2), pageable, 2L);

        when(dogRepository.findAllByDeletedFalse(pageable)).thenReturn(page);

        Page<Dog> dogs = dogService.listDogs(pageable, null, false);

        assertSame(page, dogs);
        verify(dogRepository).findAllByDeletedFalse(pageable);
        verifyNoInteractions(dogSearchRepository);

    }

    @Test
    void listDogs_nullFilterAndIncludeDeletedUsesDogRepository() {
        Dog dog1 = new Dog();
        dog1.setName("Bob");
        Dog dog2 = new Dog();
        dog2.setName("Andy");

        Page<Dog> page = Page.of(List.of(dog1, dog2), pageable, 2L);

        when(dogRepository.findAll(pageable)).thenReturn(page);

        Page<Dog> dogs = dogService.listDogs(pageable, null, true);

        assertSame(page, dogs);
        verify(dogRepository).findAll(pageable);
        verifyNoInteractions(dogSearchRepository);

    }

    @Test
    void listDogs_BlankFilterAndIncludeDeletedFalseUsesDogRepository() {
        Dog dog1 = new Dog();
        dog1.setName("Bob");
        Dog dog2 = new Dog();
        dog2.setName("Andy");

        DogFilter dogFilter = new DogFilter(Map.of("name", "", "breed", " ", "supplier", "   "));
        Page<Dog> page = Page.of(List.of(dog1, dog2), pageable, 2L);

        when(dogRepository.findAll(pageable)).thenReturn(page);

        Page<Dog> dogs = dogService.listDogs(pageable, dogFilter, true);

        assertSame(page, dogs);
        verify(dogRepository).findAll(pageable);
        verifyNoInteractions(dogSearchRepository);

    }

    @Test
    void listDogs_withFilterUsesDogSearchRepository() {
        DogFilter filter = new DogFilter(Map.of("name", "Peter",
                "breed", "German Shepherd", "supplier", "Charity A"));

        Dog dog1 = new Dog();
        dog1.setName("Peter");
        dog1.setBreed("German Shepherd");
        dog1.setSupplier("Charity A");

        Page<Dog> page = Page.of(List.of(dog1), pageable, 2L);

        when(dogSearchRepository.search(filter, false, pageable)).thenReturn(page);

        Page<Dog> result = dogService.listDogs(pageable, filter, false);

        assertSame(page, result);
        verify(dogSearchRepository).search(filter, false, pageable);
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

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> dogService.getActiveDog(15L));

        assertTrue(exception.getMessage().contains("Dog not found: 15"));

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
