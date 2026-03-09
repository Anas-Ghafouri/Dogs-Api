package com.polaris.exception;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Singleton
public class DogNotFoundExceptionHandler implements ExceptionHandler<DogNotFoundException, HttpResponse<?>> {


    @Override
    public HttpResponse<?> handle(HttpRequest request, DogNotFoundException exception) {
        return HttpResponse.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("Dog not found", exception.getMessage()));
    }
}
