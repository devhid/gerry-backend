package edu.stonybrook.cse308.gerrybackend.enums.measures;

import edu.stonybrook.cse308.gerrybackend.enums.IntEnumInterface;
import lombok.Getter;

public enum Compactness implements IntEnumInterface, MeasureInterface {
    GRAPH_THEORETICAL(0, "graph_theoretical"),
    POLSBY_POPPER(1, "polsby_popper"),
    SCHWARTZBERG(2, "schwartzberg"),
    REOCK(3, "reock"),
    CONVEX_HULL(4, "convex_hull");

    @Getter
    private final int value;
    private final String name;

    Compactness(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
