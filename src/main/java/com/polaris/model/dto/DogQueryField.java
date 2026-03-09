package com.polaris.model.dto;

import java.util.Arrays;
import java.util.Optional;

public enum DogQueryField {
    NAME("name"),
    BREED("breed"),
    SUPPLIER("supplier");

    private final String entityField;

    DogQueryField(String entityField) {
        this.entityField = entityField;
    }

    public String getEntityField() {
        return entityField;
    }

    public static Optional<DogQueryField> from(String value) {
        return Arrays.stream(values())
                .filter(field -> field.entityField.equalsIgnoreCase(value))
                .findFirst();
    }
}
