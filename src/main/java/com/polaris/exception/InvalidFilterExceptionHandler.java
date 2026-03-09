package com.polaris.exception;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Singleton
public class InvalidFilterExceptionHandler implements ExceptionHandler<InvalidFilterException, HttpResponse<?>> {

    @Override
    public HttpResponse<?> handle(HttpRequest request, InvalidFilterException exception) {
        return HttpResponse.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Invalid Filter", exception.getMessage()));
    }
}
