package edu.stonybrook.cse308.gerrybackend.enums.heuristics;

import com.fasterxml.jackson.annotation.JsonValue;
import edu.stonybrook.cse308.gerrybackend.enums.IntEnumInterface;
import lombok.Getter;

public enum PhaseOneMajMinPairs implements IntEnumInterface {
    STANDARD(0, "standard");

    @Getter
    private final int value;
    private final String name;

    PhaseOneMajMinPairs(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return this.name;
    }

}
