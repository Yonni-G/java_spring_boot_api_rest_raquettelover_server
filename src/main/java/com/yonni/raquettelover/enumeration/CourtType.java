package com.yonni.raquettelover.enumeration;

public enum CourtType {
    PADEL(4, "Padel"),
    SQUASH(2, "Squash"),
    TENNIS(2, "Tennis");

    private final int minPlayers;
    private final String label;

    CourtType(int minPlayers, String label) {
        this.minPlayers = minPlayers;
        this.label = label;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public String getLabel() {
        return label;
    }
}
