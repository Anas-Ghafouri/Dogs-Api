package com.polaris.model.entity;

public enum DogStatus implements HasLabel {
    IN_TRAINING("In Training"),
    IN_SERVICE("In Service"),
    RETIRED("Retired"),
    LEFT("Left");

    private final String label;

    DogStatus(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
