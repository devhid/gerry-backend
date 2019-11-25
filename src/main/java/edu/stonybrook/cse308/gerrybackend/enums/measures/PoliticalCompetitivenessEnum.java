package edu.stonybrook.cse308.gerrybackend.enums.measures;

import com.fasterxml.jackson.annotation.JsonValue;
import edu.stonybrook.cse308.gerrybackend.enums.IntEnumInterface;
import lombok.Getter;

public enum PoliticalCompetitivenessEnum implements IntEnumInterface, MeasureEnumInterface {
    MARGIN_OF_VICTORY(0, "margin_of_victory");

    @Getter
    private final int value;
    private final String name;

    PoliticalCompetitivenessEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return this.name;
    }
}
