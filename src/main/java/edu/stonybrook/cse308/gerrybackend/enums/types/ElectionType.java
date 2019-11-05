package edu.stonybrook.cse308.gerrybackend.enums.types;

public enum ElectionType implements IntEnumInterface {
    PRESIDENTIAL_2016(0),
    CONGRESSIONAL_2016(1),
    CONGRESSIONAL_2018(2),
    NOT_SET(3);

    public final int value;

    ElectionType(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }

    public static ElectionType getDefault(){
        return ElectionType.NOT_SET;
    }

    public static int getMaxValue(){
        int max = 0;
        for (ElectionType type : ElectionType.values()){
            if (type != ElectionType.getDefault() && type.getValue() > max){
                max = type.getValue();
            }
        }
        return max;
    }
}
