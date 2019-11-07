package edu.stonybrook.cse308.gerrybackend.enums.types;

public enum DemographicType implements IntEnumInterface {
    NH_WHITE(0),
    H_WHITE(1),
    NH_BLACK(2),
    H_BLACK(3),
    NH_ASIAN(4),
    H_ASIAN(5),
    HISPANIC(6),
    NH_PACIFIC_ISLANDER(7),
    H_PACIFIC_ISLANDER(8),
    NH_NATIVE_AMERICAN(9),
    H_NATIVE_AMERICAN(10),
    BIRACIAL(11),
    OTHER(12),
    ALL(13);

    public final int value;

    DemographicType(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }

    public static DemographicType getDefault(){
        return null;
    }

}
