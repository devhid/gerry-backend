package edu.stonybrook.cse308.gerrybackend.enums.heuristics;

import com.fasterxml.jackson.annotation.JsonValue;
import edu.stonybrook.cse308.gerrybackend.enums.IntEnumInterface;
import lombok.Getter;

public enum PhaseTwoPrecinctMove implements IntEnumInterface {
    RANDOM(0, "random"),
    MAJ_MIN(1, "maj_min");

    @Getter
    private final int value;
    private final String name;

    PhaseTwoPrecinctMove(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return this.name;
    }
}
