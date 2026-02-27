package com.polaris.error;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Singleton;

import java.util.NoSuchElementException;

@Singleton
public class NoSuchElementExceptionHandler implements ExceptionHandler<NoSuchElementException, HttpResponse<?>> {


    @Override
    public HttpResponse<?> handle(HttpRequest request, NoSuchElementException exception) {
        return HttpResponse.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("Dog not found", exception.getMessage()));
    }

    @Serdeable
    public record ErrorResponse(String error, String message) {}
}
