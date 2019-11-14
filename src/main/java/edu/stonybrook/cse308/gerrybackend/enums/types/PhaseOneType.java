package edu.stonybrook.cse308.gerrybackend.enums.types;

import com.fasterxml.jackson.annotation.JsonValue;
import edu.stonybrook.cse308.gerrybackend.enums.IntEnumInterface;
import lombok.Getter;

public enum PhaseOneType implements IntEnumInterface {
    STANDARD(0, "standard"),
    ALT_ONE(1, "alt_one");

    @Getter
    private final int value;
    private final String name;

    PhaseOneType(int value, String name){
        this.value = value;
        this.name = name;
    }

    @JsonValue
    public String getName(){
        return this.name;
    }

}
