package edu.stonybrook.cse308.gerrybackend.enums.types;

import com.fasterxml.jackson.annotation.JsonValue;
import edu.stonybrook.cse308.gerrybackend.enums.IntEnumInterface;
import lombok.Getter;

public enum ElectionType implements IntEnumInterface {
    PRESIDENTIAL_2016(0, "pres_16"),
    CONGRESSIONAL_2016(1, "house_16"),
    CONGRESSIONAL_2018(2, "house_18"),
    NOT_SET(3, "not_set");

    @Getter
    private final int value;
    private final String name;

    ElectionType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return this.name;
    }

    public static ElectionType getDefault() {
        return ElectionType.NOT_SET;
    }

}
