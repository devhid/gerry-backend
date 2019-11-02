package edu.stonybrook.cse308.gerrybackend.enums;

public enum ElectionType {
    PRESIDENTIAL_2016(0),
    CONGRESSIONAL_2016(1),
    CONGRESSIONAL_2018(2);

    public final int value;

    ElectionType(int value){
        this.value = value;
    }
}
