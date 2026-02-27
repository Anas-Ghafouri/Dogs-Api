package com.polaris.model.entity;

public enum DogLeavingReason implements HasLabel {

    TRANSFERRED("Transferred"),
    RETIRED_PUT_DOWN("Retired (Put Down)"),
    KIA("KIA"),
    REJECTED("Rejected"),
    RETIRED_REHOUSED("Retired (Re-housed)"),
    DIED("Died");

    private final String label;

    DogLeavingReason(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
