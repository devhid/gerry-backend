package edu.stonybrook.cse308.gerrybackend.enums.types;

import com.fasterxml.jackson.annotation.JsonValue;
import edu.stonybrook.cse308.gerrybackend.enums.IntEnumInterface;
import lombok.Getter;

public enum DemographicType implements IntEnumInterface {
    NH_WHITE(0, "pop_white_nh"),
    NH_BLACK(1, "pop_black_nh"),
    NH_ASIAN(2, "pop_asian_nh"),
    HISPANIC(3, "pop_hispanic"),
    NH_PACIFIC_ISLANDER(4, "pop_nhpi_nh"),
    NH_NATIVE_AMERICAN(5, "pop_amin_nh"),
    NH_TWO_OR_MORE(6, "pop_2more_nh"),
    NH_OTHER(7, "nh_other"),

    @Getter
    private final int value;
    private final String name;

    DemographicType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return this.name;
    }

    public static DemographicType getDefault() {
        return null;
    }

}
