package edu.stonybrook.cse308.gerrybackend.enums.measures;

import com.fasterxml.jackson.annotation.JsonValue;
import edu.stonybrook.cse308.gerrybackend.enums.IntEnumInterface;
import lombok.Getter;

public enum CompactnessEnum implements IntEnumInterface, MeasureEnumInterface {
    GRAPH_THEORETICAL(0, "graph_theoretical"),
    POLSBY_POPPER(1, "polsby_popper"),
    SCHWARTZBERG(2, "schwartzberg");

    @Getter
    private final int value;
    private final String name;

    CompactnessEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return this.name;
    }
}
