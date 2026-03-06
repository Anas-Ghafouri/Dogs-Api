package com.polaris.controller;

import com.polaris.model.dto.DogFilter;
import com.polaris.model.dto.DogResponse;
import com.polaris.model.entity.Dog;
import com.polaris.model.mapper.DogMapper;
import com.polaris.service.DogService;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class DogControllerTest {

    @Mock
    DogService dogService;

    @Mock
    DogMapper dogMapper;

    @InjectMocks
    DogController dogController;

    Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = Pageable.from(0, 10);
    }

    @Test
    void listDogs_emptyFilterPassesNullDogFilterToService() {
        Dog dog1 = new Dog();
        Dog dog2 = new Dog();
        Page<Dog> page = Page.of(List.of(dog1, dog2), pageable, 2L);

        when(dogService.listDogs(pageable, null, false)).thenReturn(page);

        Page<DogResponse> result = dogController.listDogs("name:, breed: , supplier:  ", false, pageable);

        assertEquals(2, result.getContent().size());
        verify(dogService).listDogs(pageable, null, false);
        verify(dogMapper, times(2)).entityToResponse(any(Dog.class));
    }

    @Test
    void listDogs_validFilterParsedAndPassedToService() {
        Dog dog = new Dog();
        dog.setName("Rex");
        String filter = "name: Rex, breed: Labrador, supplier: Charity A";
        Page<Dog> page = Page.of(List.of(dog), pageable, 1L);

        when(dogService.listDogs(any(Pageable.class), any(DogFilter.class), eq(false))).thenReturn(page);

        dogController.listDogs(filter, false, pageable);

        ArgumentCaptor<DogFilter> captor = ArgumentCaptor.forClass(DogFilter.class);
        verify(dogService).listDogs(eq(pageable), captor.capture(), eq(false));

        DogFilter captorValue = captor.getValue();

        assertNotNull(captorValue);

        Map<String, String> stringFilters = captorValue.stringFilters();
        assertEquals(3, stringFilters.size());
        assertEquals("Rex", stringFilters.get("name"));
        assertEquals("Labrador", stringFilters.get("breed"));
        assertEquals("Charity A", stringFilters.get("supplier"));
    }
}
