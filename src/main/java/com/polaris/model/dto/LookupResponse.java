package com.polaris.model.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record LookupResponse(
        String label,
        String value
) {}
