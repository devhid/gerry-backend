package edu.stonybrook.cse308.gerrybackend.enums.measures;

import edu.stonybrook.cse308.gerrybackend.enums.IntEnumInterface;
import lombok.Getter;

public enum PoliticalCompetitiveness implements IntEnumInterface, MeasureInterface {
    MARGIN_OF_VICTORY(0, "margin_of_victory");

    @Getter
    private final int value;
    private final String name;

    PoliticalCompetitiveness(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
