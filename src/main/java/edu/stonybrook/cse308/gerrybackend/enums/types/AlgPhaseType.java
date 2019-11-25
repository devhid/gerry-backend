package edu.stonybrook.cse308.gerrybackend.enums.types;

import com.fasterxml.jackson.annotation.JsonValue;
import edu.stonybrook.cse308.gerrybackend.enums.IntEnumInterface;
import lombok.Getter;

public enum AlgPhaseType implements IntEnumInterface {
    PHASE_ZERO(0, "phase_zero"),
    PHASE_ONE(1, "phase_one"),
    PHASE_TWO(2, "phase_two");

    @Getter
    private final int value;
    private final String name;

    AlgPhaseType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return this.name;
    }

}
