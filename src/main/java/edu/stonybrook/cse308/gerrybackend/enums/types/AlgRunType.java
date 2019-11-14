package edu.stonybrook.cse308.gerrybackend.enums.types;

import com.fasterxml.jackson.annotation.JsonValue;
import edu.stonybrook.cse308.gerrybackend.enums.IntEnumInterface;
import lombok.Getter;

public enum AlgRunType implements IntEnumInterface {
    TO_COMPLETION(0, "to_completion"),
    BY_STEP(1, "by_step");

    @Getter
    private final int value;
    private final String name;

    AlgRunType(int value, String name){
        this.value = value;
        this.name = name;
    }

    @JsonValue
    public String getName(){
        return this.name;
    }
}
