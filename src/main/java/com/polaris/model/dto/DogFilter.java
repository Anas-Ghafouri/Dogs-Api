package com.polaris.model.dto;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public record DogFilter(
        Map<String, String> stringFilters
) {

    private static final Set<String> STRING_FILTERS = Set.of(
            "name",
            "breed",
            "supplier"
    );

    public static DogFilter from(String rawFilter) {
        if (rawFilter == null || rawFilter.isEmpty()) {
            return null;
        }

        var parsedFilter = parseFilter(rawFilter);
        var authorisedFilters = authoriseFilters(parsedFilter);

        return authorisedFilters.isEmpty() ? null : new DogFilter(authorisedFilters);
    }

    private static Map<String, String> parseFilter(String rawFilter) {

        return Arrays.stream(rawFilter.split(","))
                .map(String::trim)
                .map(str -> str.split(":", 2))
                .filter(entry -> entry.length == 2)
                .collect(Collectors.toMap(
                        entry -> entry[0].trim().toLowerCase(),
                        entry -> entry[1].trim(),
                        (a,b) -> b
                ));

    }

    private static Map<String, String> authoriseFilters(Map<String, String> parsedFilters) {
        Map<String, String> authorisedFilters = new HashMap<>();

        parsedFilters.forEach((key, value) -> {
            if (value != null && !value.isBlank() && STRING_FILTERS.contains(key)) {
                authorisedFilters.put(key, value.trim());
            }
        });
        return authorisedFilters;
    }

    public boolean hasNoFilters() {
        if (stringFilters == null || stringFilters.isEmpty()) return true;
        return stringFilters.values().stream().allMatch(v -> v == null || v.isBlank());
    }
}
