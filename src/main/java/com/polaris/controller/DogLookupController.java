package com.polaris.controller;

import com.polaris.model.dto.LookupValueResponse;
import com.polaris.model.entity.DogLeavingReason;
import com.polaris.model.entity.DogStatus;
import com.polaris.model.entity.DogGender;
import com.polaris.model.entity.HasLabel;
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
        return toLookupResponse(DogGender.values());
    }

    @Get("/leaving-reasons")
    public List<LookupValueResponse> leavingReasons() {
        return toLookupResponse(DogLeavingReason.values());
    }

    private <E extends Enum<E> & HasLabel> List<LookupValueResponse> toLookupResponse(E[] values) {
        return Arrays.stream(values)
                .map(value -> new LookupValueResponse(value.getLabel(), value.name()))
                .toList();
    }
}
