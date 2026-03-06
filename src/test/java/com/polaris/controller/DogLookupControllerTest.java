package com.polaris.controller;

import com.polaris.model.dto.LookupResponse;
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
        List<LookupResponse> response = getLookupResponses("/statuses");

        assertNotNull(response);
        assertEquals(DogStatus.values().length, response.size());

        Map<String, LookupResponse> byValueMap = listToMap(response);

        for (DogStatus status : DogStatus.values()) {
            LookupResponse lookupResponse = byValueMap.get(status.name());
            assertNotNull(lookupResponse);
            assertEquals(status.getLabel(), lookupResponse.label());
            assertEquals(status.name(), lookupResponse.value());
        }

    }

    @Test
    void genders_returnsAllGenders() {
        List<LookupResponse> response = getLookupResponses("/genders");

        assertNotNull(response);
        assertEquals(DogGender.values().length, response.size());

        Map<String, LookupResponse> byValueMap = listToMap(response);

        for (DogGender gender : DogGender.values()) {
            LookupResponse lookupResponse = byValueMap.get(gender.name());
            assertNotNull(lookupResponse);
            assertEquals(gender.getLabel(), lookupResponse.label());
            assertEquals(gender.name(), lookupResponse.value());
        }

    }

    @Test
    void leavingReasons_returnsAllLeavingReasons() {
        List<LookupResponse> response = getLookupResponses("/leaving-reasons");

        assertNotNull(response);
        assertEquals(DogLeavingReason.values().length, response.size());

        Map<String, LookupResponse> byValueMap = listToMap(response);

        for (DogLeavingReason leavingReason : DogLeavingReason.values()) {
            LookupResponse lookupResponse = byValueMap.get(leavingReason.name());
            assertNotNull(lookupResponse);
            assertEquals(leavingReason.getLabel(), lookupResponse.label());
            assertEquals(leavingReason.name(), lookupResponse.value());
        }

    }

    private List<LookupResponse> getLookupResponses(String string) {
        return client.toBlocking().retrieve(
                HttpRequest.GET(BASE_URL + string),
                Argument.listOf(LookupResponse.class)
        );
    }

    private static Map<String, LookupResponse> listToMap(List<LookupResponse> response) {
        return response.stream()
                .collect(Collectors.toMap(LookupResponse::value, Function.identity()));
    }


}
