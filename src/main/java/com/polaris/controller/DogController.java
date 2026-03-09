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

import jakarta.validation.Valid;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Status;

@Controller(DogController.API_BASE)
public class DogController {

    private final DogService dogService;
    private final DogMapper dogMapper;

    public DogController(DogService dogService, DogMapper dogMapper) {
        this.dogService = dogService;
        this.dogMapper = dogMapper;
    }

    public static final String API_BASE = "/api/dogs";

    @Get("/dogs")
    public Page<DogResponse> listDogs(@QueryValue @Nullable String filter,
                                      @QueryValue(defaultValue = "false") boolean includeDeleted,
                                      Pageable pageable) {

        DogFilter dogFilter = DogFilter.from(filter);

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
}
