package com.polaris.model.entity;

public enum LeavingReason implements Labeled {

    TRANSFERRED("Transferred"),
    RETIRED_PUT_DOWN("Retired (Put Down)"),
    KIA("KIA"),
    REJECTED("Rejected"),
    RETIRED_REHOUSED("Retired (Re-housed)"),
    DIED("Died");

    private final String label;

    LeavingReason(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
