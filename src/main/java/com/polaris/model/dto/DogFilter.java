package com.polaris.model.dto;

import com.polaris.exception.InvalidFilterException;

import java.util.*;
import java.util.stream.Collectors;

public record DogFilter(
        Map<DogQueryField, String> filters
) {

    public DogFilter {
        filters = filters == null ? Map.of() : Map.copyOf(filters);
    }

    public static DogFilter from(String rawFilter) {
        if (rawFilter == null || rawFilter.isBlank()) {
            return new DogFilter(Map.of());
        }

        HashMap<DogQueryField, String> parsedMap = new HashMap<>();

        for (String rawEntry : rawFilter.split(",")) {
            String entry = rawEntry.trim();

            String[] parts = entry.split(":", 2);
            if (parts.length != 2) {
                throw new InvalidFilterException(
                        "Invalid filter format '%s'. Expected field:value".formatted(entry)
                );
            }

            String fieldPart = parts[0].trim().toLowerCase(Locale.ROOT);
            String valuePart = parts[1].trim();

            if (valuePart.isBlank()) {
                throw new InvalidFilterException(
                        "Invalid filter format '%s'. Filter value must not be blank.".formatted(entry)
                );
            }

            DogQueryField dogQueryField = DogQueryField.from(fieldPart)
                    .orElseThrow(() -> new InvalidFilterException(
                            "Invalid filter field '%s'.".formatted(fieldPart)
                    ));

            parsedMap.put(dogQueryField, valuePart);
        }
        return new DogFilter(parsedMap);
    }

    public boolean hasNoFilters() {
        return filters.isEmpty();
    }
}
