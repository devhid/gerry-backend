package edu.stonybrook.cse308.gerrybackend.enums.types;

import com.fasterxml.jackson.annotation.JsonValue;
import edu.stonybrook.cse308.gerrybackend.enums.IntEnumInterface;
import lombok.Getter;

public enum LikelyType implements IntEnumInterface {
    NOT(0, "not_likely"),
    KIND_OF(1, "kind_of_likely"),
    VERY(2, "very_likely");

    @Getter
    private final int value;
    private final String name;

    LikelyType(int value, String name){
        this.value = value;
        this.name = name;
    }

    @JsonValue
    public String getName(){
        return this.name;
    }

}