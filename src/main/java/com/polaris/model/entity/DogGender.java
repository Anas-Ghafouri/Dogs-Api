package com.polaris.model.entity;

public enum DogGender implements Labeled {
    MALE("male"),
    FEMALE("female"),
    UNKNOWN("unknown");

    private final String label;

    DogGender(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
