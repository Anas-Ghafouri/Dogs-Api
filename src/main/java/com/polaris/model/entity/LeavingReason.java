package com.polaris.model.entity;

public enum LeavingReason {

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

    public String getLabel() {
        return label;
    }
}
