package com.polaris.model.dto;

import com.polaris.exception.InvalidFilterException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DogFilterTest {

    @Test
    void from_validFilter_parsesFilters() {
        DogFilter dogFilter = DogFilter.from("name:Rex, breed:Labrador, supplier:Charity A");

        assertFalse(dogFilter.hasNoFilters());

        Map<DogQueryField, String> filters = dogFilter.filters();
        assertEquals(3, filters.size());
        assertEquals("Rex", filters.get(DogQueryField.NAME));
        assertEquals("Labrador", filters.get(DogQueryField.BREED));
        assertEquals("Charity A", filters.get(DogQueryField.SUPPLIER));
    }

    @Test
    void blankValue_throwsInvalidFilterException() {
        InvalidFilterException exception = assertThrows(
                InvalidFilterException.class,
                () -> DogFilter.from("name:")
        );

        assertEquals(
                "Invalid filter format 'name:'. Filter value must not be blank.",
                exception.getMessage()
        );
    }

    @Test
    void missingColon_throwsInvalidFilterException() {
        InvalidFilterException exception = assertThrows(
                InvalidFilterException.class,
                () -> DogFilter.from("name")
        );

        assertEquals(
                "Invalid filter format 'name'. Expected field:value",
                exception.getMessage()
        );
    }

    @Test
    void invalidFilterField_throwsInvalidFilterException() {
        InvalidFilterException exception = assertThrows(
                InvalidFilterException.class,
                () -> DogFilter.from("size:big")
        );

        assertEquals(
                "Invalid filter field 'size'.",
                exception.getMessage()
        );
    }


}