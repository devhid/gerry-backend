package edu.stonybrook.cse308.gerrybackend.enums.types;

import com.fasterxml.jackson.annotation.JsonValue;
import edu.stonybrook.cse308.gerrybackend.enums.IntEnumInterface;
import lombok.Getter;

public enum StateType implements IntEnumInterface {
    CALIFORNIA(0, "CA"),
    UTAH(1, "UT"),
    VIRGINIA(2, "VA"),
    NOT_SET(3, "not_set");

    @Getter
    private final int value;

    private final String name;

    StateType(int value, String name){
        this.value = value;
        this.name = name;
    }

    @JsonValue
    public String getName(){
        return this.name;
    }

    public static StateType getDefault(){
        return NOT_SET;
    }
}