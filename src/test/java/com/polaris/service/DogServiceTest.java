package com.polaris.service;

import com.polaris.model.dto.DogFilter;
import com.polaris.model.dto.DogRequest;
import com.polaris.model.entity.Dog;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
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

//    @Test
//    void listDogs_nullFilterAndNotIncludeDeleted_usesDogRepository() {
//        Dog dog1 = new Dog();
//        dog1.setName("Bob");
//
//        Dog dog2 = new Dog();
//        dog2.setName("Andy");
//
//        Page<Dog> page = Page.of(List.of(dog1, dog2), pageable, 2L);
//
//        when(dogRepository.findAllByDeletedFalse(pageable)).thenReturn(page);
//
//        Page<Dog> dogs = dogService.listDogs(pageable, null, false);
//
//        assertSame(page, dogs);
//        verify(dogRepository).findAllByDeletedFalse(pageable);
//        verifyNoInteractions(dogSearchRepository);
//
//    }
//
//    @Test
//    void listDogs_withFilter_usesSearchRepository() {
//        DogFilter filter = new DogFilter("Bob", "German Shepherd", "Charity");
//        Dog dog1 = new Dog();
//        dog1.setName("Bob");
//
//        Page<Dog> page = Page.of(List.of(dog1), pageable, 1L);
//
//        when(dogSearchRepository.search(filter, false, pageable)).thenReturn(page);
//
//        Page<Dog> dogs = dogService.listDogs(pageable, filter, false);
//
//        assertSame(page, dogs);
//        verify(dogSearchRepository).search(filter, false, pageable);
//        verify(dogRepository, never()).findAllByDeletedFalse(any());
//
//    }



}
