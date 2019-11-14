package edu.stonybrook.cse308.gerrybackend.enums.measures;

import com.fasterxml.jackson.annotation.JsonValue;
import edu.stonybrook.cse308.gerrybackend.enums.IntEnumInterface;
import lombok.Getter;

public enum PopulationEqualityEnum implements IntEnumInterface, MeasureEnumInterface {
    MOST_TO_LEAST(0, "most_to_least");

    @Getter
    private final int value;
    private final String name;

    PopulationEqualityEnum(int value, String name){
        this.value = value;
        this.name = name;
    }

    @JsonValue
    public String getName(){
        return this.name;
    }
}
