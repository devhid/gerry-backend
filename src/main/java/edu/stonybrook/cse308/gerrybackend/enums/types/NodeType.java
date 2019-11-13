package edu.stonybrook.cse308.gerrybackend.enums.types;

import com.fasterxml.jackson.annotation.JsonValue;
import edu.stonybrook.cse308.gerrybackend.enums.IntEnumInterface;
import lombok.Getter;

public enum NodeType implements IntEnumInterface {
    ORIGINAL(0, "original"),
    USER(1, "user"),
    NOT_SET(2, "not_set");

    @Getter
    private final int value;

    private final String name;

    NodeType(int value, String name){
        this.value = value;
        this.name = name;
    }

    @JsonValue
    public String getName(){
        return this.name;
    }

    public static NodeType getDefault(){
        return NodeType.NOT_SET;
    }
}
