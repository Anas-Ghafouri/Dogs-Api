package com.polaris.model.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record LookupValueResponse(
        String label,
        String value
) {}
