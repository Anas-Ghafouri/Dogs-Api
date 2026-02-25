package com.polaris.model.entity;

public enum Gender implements Labeled {
    MALE("male"),
    FEMALE("female"),
    UNKNOWN("unknown");

    private final String label;

    Gender(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
