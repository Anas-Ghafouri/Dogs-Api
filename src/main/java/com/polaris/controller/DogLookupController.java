package com.polaris.controller;

import com.polaris.model.dto.LookupValueResponse;
import com.polaris.model.entity.DogStatus;
import com.polaris.model.entity.Gender;
import com.polaris.model.entity.Labeled;
import com.polaris.model.entity.LeavingReason;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.util.Arrays;
import java.util.List;

@Controller("/api/dogs/lookups")
public class DogLookupController {

    @Get("/statuses")
    public List<LookupValueResponse> statuses() {
        return toLookupResponse(DogStatus.values());
    }

    @Get("/genders")
    public List<LookupValueResponse> genders() {
        return toLookupResponse(Gender.values());
    }

    @Get("/leaving-reasons")
    public List<LookupValueResponse> leavingReasons() {
        return toLookupResponse(LeavingReason.values());
    }

    private <E extends Enum<E> & Labeled> List<LookupValueResponse> toLookupResponse(E[] values) {
        return Arrays.stream(values)
                .map(value -> new LookupValueResponse(value.getLabel(), value.name()))
                .toList();
    }
}
