package edu.stonybrook.cse308.gerrybackend.enums.heuristics;

import com.fasterxml.jackson.annotation.JsonValue;
import edu.stonybrook.cse308.gerrybackend.enums.IntEnumInterface;
import lombok.Getter;

public enum PhaseOneStop implements IntEnumInterface {
    JOIN_SMALLEST(0, "join_smallest");

    @Getter
    private final int value;
    private final String name;

    PhaseOneStop(int value, String name){
        this.value = value;
        this.name = name;
    }

    @JsonValue
    public String getName(){
        return this.name;
    }
}
