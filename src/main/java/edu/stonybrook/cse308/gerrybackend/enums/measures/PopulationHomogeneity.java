package edu.stonybrook.cse308.gerrybackend.enums.measures;

import com.fasterxml.jackson.annotation.JsonValue;
import edu.stonybrook.cse308.gerrybackend.enums.IntEnumInterface;
import lombok.Getter;

public enum PopulationHomogeneity implements IntEnumInterface, MeasureInterface {
    NORMALIZED_SQUARE_ERROR(0, "normalized_square_error");

    @Getter
    private final int value;
    private final String name;

    PopulationHomogeneity(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return this.name;
    }
}
