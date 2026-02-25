package com.polaris.controller;

import com.polaris.model.dto.DogFilter;
import com.polaris.model.dto.DogRequest;
import com.polaris.model.dto.DogResponse;
import com.polaris.model.mapper.DogMapper;
import com.polaris.service.DogService;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import jakarta.validation.Valid;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Controller("/api/dogs")
public class DogController {

    private final DogService dogService;
    private final DogMapper dogMapper;

    public DogController(DogService dogService, DogMapper dogMapper) {
        this.dogService = dogService;
        this.dogMapper = dogMapper;
    }

    @Get("/dogs")
    public Page<DogResponse> listDogs(@QueryValue @Nullable String filter,
                                      @QueryValue(defaultValue = "false") boolean includeDeleted,
                                      Pageable pageable) {

        DogFilter dogFilter = (filter != null && !filter.isBlank())
                ? mapToDogFilter(parseFilter(filter))
                : null;
        return dogService.listDogs(pageable, dogFilter, includeDeleted)
                 .map(dogMapper::entityToResponse);
    }

    @Get("/{id}")
    public DogResponse getDog(Long id) {

        return dogMapper.entityToResponse(dogService.getActiveDog(id));
    }

    @Post
    public HttpResponse<DogResponse> createDog(@Body @Valid DogRequest request) {

        var created = dogService.createDog(request);
        var response = dogMapper.entityToResponse(created);

        return HttpResponse.created(response);
    }

    @Put("/{id}")
    public DogResponse updateDog(Long id, @Body @Valid DogRequest request) {

        return dogMapper.entityToResponse(dogService.updateDog(id, request));
    }

    @Delete("/{id}")
    @Status(HttpStatus.NO_CONTENT)
    public void deleteDog(Long id) {

        dogService.deleteDog(id);
    }

    private DogFilter mapToDogFilter(Map<String, String> paramValues) {

        return new DogFilter(
                paramValues.get("name"),
                paramValues.get("breed"),
                paramValues.get("supplier")
        );
    }

    private Map<String, String> parseFilter(String filter) {

        return Arrays.stream(filter.split(","))
                .map(String::trim)
                .map(str -> str.split(":", 2))
                .filter(entry -> entry.length == 2)
                .collect(Collectors.toMap(
                        entry -> entry[0].trim().toLowerCase(),
                        entry -> entry[1].trim()
                ));

    }

}
