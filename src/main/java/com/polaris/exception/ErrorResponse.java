package com.polaris.exception;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record ErrorResponse (String error, String message) {}

