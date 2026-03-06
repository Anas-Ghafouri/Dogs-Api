package com.polaris.controller;

import com.polaris.model.dto.LookupResponse;
import com.polaris.model.entity.*;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.util.Arrays;
import java.util.List;

@Controller(DogController.API_BASE + "/lookups")
public class DogLookupController {

    @Get("/statuses")
    public List<LookupResponse> statuses() {
        return toLookupResponse(DogStatus.values());
    }

    @Get("/genders")
    public List<LookupResponse> genders() {
        return toLookupResponse(DogGender.values());
    }

    @Get("/leaving-reasons")
    public List<LookupResponse> leavingReasons() {
        return toLookupResponse(DogLeavingReason.values());
    }

    private <E extends Enum<E> & HasLabel> List<LookupResponse> toLookupResponse(E[] values) {
        return Arrays.stream(values)
                .map(value -> new LookupResponse(value.getLabel(), value.name()))
                .toList();
    }
}
