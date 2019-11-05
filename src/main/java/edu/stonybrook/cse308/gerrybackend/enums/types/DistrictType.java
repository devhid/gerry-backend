package edu.stonybrook.cse308.gerrybackend.enums.types;

public enum DistrictType implements IntEnumInterface {
    ORIGINAL_DISTRICT(0),
    USER_DISTRICT(1),
    NOT_SET(2);

    public final int value;

    DistrictType(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }

    public static DistrictType getDefault(){
        return DistrictType.NOT_SET;
    }

    public static int getMaxValue(){
        int max = 0;
        for (DistrictType type : DistrictType.values()){
            if (type != DistrictType.getDefault() && type.getValue() > max){
                max = type.getValue();
            }
        }
        return max;
    }
}
