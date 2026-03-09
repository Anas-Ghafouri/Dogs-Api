package com.polaris.controller;

import com.polaris.model.dto.DogLookupResponse;
import com.polaris.model.entity.DogGender;
import com.polaris.model.entity.DogLeavingReason;
import com.polaris.model.entity.DogStatus;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
public class DogLookupControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    private static final String BASE_URL = "/api/dogs/lookups";

    @Test
    void statuses_returnsAllStatuses() {
        List<DogLookupResponse> response = getLookupResponses("/statuses");

        assertNotNull(response);
        assertEquals(DogStatus.values().length, response.size());

        Map<String, DogLookupResponse> byValueMap = listToMap(response);

        for (DogStatus status : DogStatus.values()) {
            DogLookupResponse dogLookupResponse = byValueMap.get(status.name());
            assertNotNull(dogLookupResponse);
            assertEquals(status.getLabel(), dogLookupResponse.label());
            assertEquals(status.name(), dogLookupResponse.value());
        }

    }

    @Test
    void genders_returnsAllGenders() {
        List<DogLookupResponse> response = getLookupResponses("/genders");

        assertNotNull(response);
        assertEquals(DogGender.values().length, response.size());

        Map<String, DogLookupResponse> byValueMap = listToMap(response);

        for (DogGender gender : DogGender.values()) {
            DogLookupResponse dogLookupResponse = byValueMap.get(gender.name());
            assertNotNull(dogLookupResponse);
            assertEquals(gender.getLabel(), dogLookupResponse.label());
            assertEquals(gender.name(), dogLookupResponse.value());
        }

    }

    @Test
    void leavingReasons_returnsAllLeavingReasons() {
        List<DogLookupResponse> response = getLookupResponses("/leaving-reasons");

        assertNotNull(response);
        assertEquals(DogLeavingReason.values().length, response.size());

        Map<String, DogLookupResponse> byValueMap = listToMap(response);

        for (DogLeavingReason leavingReason : DogLeavingReason.values()) {
            DogLookupResponse dogLookupResponse = byValueMap.get(leavingReason.name());
            assertNotNull(dogLookupResponse);
            assertEquals(leavingReason.getLabel(), dogLookupResponse.label());
            assertEquals(leavingReason.name(), dogLookupResponse.value());
        }

    }

    private List<DogLookupResponse> getLookupResponses(String string) {
        return client.toBlocking().retrieve(
                HttpRequest.GET(BASE_URL + string),
                Argument.listOf(DogLookupResponse.class)
        );
    }

    private static Map<String, DogLookupResponse> listToMap(List<DogLookupResponse> response) {
        return response.stream()
                .collect(Collectors.toMap(DogLookupResponse::value, Function.identity()));
    }


}
