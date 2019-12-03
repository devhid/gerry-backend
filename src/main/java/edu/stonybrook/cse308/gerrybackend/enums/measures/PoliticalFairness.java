package edu.stonybrook.cse308.gerrybackend.enums.measures;

import edu.stonybrook.cse308.gerrybackend.enums.IntEnumInterface;
import lombok.Getter;

public enum PoliticalFairness implements IntEnumInterface, MeasureInterface {
    EFFICIENCY_GAP(0, "efficiency_gap"),
    GERRYMANDER_DEMOCRAT(1, "gerrymander_democrat"),
    GERRYMANDER_REPUBLICAN(2, "gerrymander_republican"),
    LOPSIDED_MARGINS(3, "lopsided_margins"),
    MEAN_MEDIAN(4, "mean_median"),
    PARTISAN_DEMOCRAT(5, "partisan_democrat"),
    PARTISAN_REPUBLICAN(6, "partisan_republican");

    @Getter
    private final int value;
    private final String name;

    PoliticalFairness(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
