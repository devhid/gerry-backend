package edu.stonybrook.cse308.gerrybackend.enums.types;

import com.fasterxml.jackson.annotation.JsonValue;
import edu.stonybrook.cse308.gerrybackend.enums.IntEnumInterface;
import lombok.Getter;

public enum StatusCode implements IntEnumInterface {
    IN_PROGRESS(0, "in_progress"),
    SUCCESS(1, "success"),
    FAILURE(2, "failure");

    @Getter
    private final int value;
    private final String name;

    StatusCode(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return this.name;
    }

}
