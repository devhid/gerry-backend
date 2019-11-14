package edu.stonybrook.cse308.gerrybackend.enums.types;

import com.fasterxml.jackson.annotation.JsonValue;
import edu.stonybrook.cse308.gerrybackend.enums.IntEnumInterface;
import lombok.Getter;

public enum PoliticalParty implements IntEnumInterface {
    DEMOCRATIC(0, "democratic"),
    REPUBLICAN(1, "republican"),
    INDEPENDENT(2, "independent"),
    OTHER(3, "other"),
    TIE(4, "tie"),
    NOT_SET(5, "not_set");

    @Getter
    private final int value;
    private final String name;

    PoliticalParty(int value, String name){
        this.value = value;
        this.name = name;
    }

    @JsonValue
    public String getName(){
        return this.name;
    }

    public static PoliticalParty getDefault(){
        return PoliticalParty.NOT_SET;
    }
}
