package com.polaris.model.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record DogLookupResponse(
        String label,
        String value
) {}
