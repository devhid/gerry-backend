package edu.stonybrook.cse308.gerrybackend.enums.types;

import com.fasterxml.jackson.annotation.JsonValue;
import edu.stonybrook.cse308.gerrybackend.enums.IntEnumInterface;
import lombok.Getter;

public enum DemographicType implements IntEnumInterface {
    NH_WHITE(0, "nonhispanic_white"),
    H_WHITE(1, "hispanic_white"),
    NH_BLACK(2, "nonhispanic_black"),
    H_BLACK(3, "hispanic_black"),
    NH_ASIAN(4, "nonhispanic_asian"),
    H_ASIAN(5, "hispanic_asian"),
    HISPANIC(6, "hispanic"),
    NH_PACIFIC_ISLANDER(7, "nonhispanic_pacific_islander"),
    H_PACIFIC_ISLANDER(8, "hispanic_pacific_islander"),
    NH_NATIVE_AMERICAN(9, "nonhispanic_native_american"),
    H_NATIVE_AMERICAN(10, "hispanic_native_american"),
    BIRACIAL(11, "biracial"),
    OTHER(12, "other"),
    ALL(13, "all");

    @Getter
    private final int value;

    private final String name;

    DemographicType(int value, String name){
        this.value = value;
        this.name = name;
    }

    @JsonValue
    public String getName(){
        return this.name;
    }

    public static DemographicType getDefault(){
        return null;
    }

}
