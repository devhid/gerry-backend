package edu.stonybrook.cse308.gerrybackend.enums.types;

import com.fasterxml.jackson.annotation.JsonValue;
import edu.stonybrook.cse308.gerrybackend.enums.IntEnumInterface;
import lombok.Getter;

public enum PhaseTwoType implements IntEnumInterface {
    STANDARD(0, "standard"),
    ALT_ONE(1, "alt_one"),
    ALT_TWO(2, "alt_two");

    @Getter
    private final int value;
    private final String name;

    PhaseTwoType(int value, String name){
        this.value = value;
        this.name = name;
    }

    @JsonValue
    public String getName(){
        return this.name;
    }

}