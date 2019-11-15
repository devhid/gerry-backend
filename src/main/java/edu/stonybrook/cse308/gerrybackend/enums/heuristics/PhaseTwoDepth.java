package edu.stonybrook.cse308.gerrybackend.enums.heuristics;

import com.fasterxml.jackson.annotation.JsonValue;
import edu.stonybrook.cse308.gerrybackend.enums.IntEnumInterface;
import lombok.Getter;

public enum PhaseTwoDepth implements IntEnumInterface {
    STANDARD(0, "standard"),
    LEVEL(1, "level"),
    TREE(2, "tree");

    @Getter
    private final int value;
    private final String name;

    PhaseTwoDepth(int value, String name){
        this.value = value;
        this.name = name;
    }

    @JsonValue
    public String getName(){
        return this.name;
    }

}