package edu.stonybrook.cse308.gerrybackend.enums.types;

import edu.stonybrook.cse308.gerrybackend.enums.IntEnumInterface;

public enum StateType implements IntEnumInterface {
    CALIFORNIA(0),
    UTAH(1),
    VIRGINIA(2),
    NOT_SET(3);

    public final int value;

    StateType(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }

    public static StateType getDefault(){
        return NOT_SET;
    }
}