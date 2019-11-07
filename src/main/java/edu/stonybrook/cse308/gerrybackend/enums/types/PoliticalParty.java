package edu.stonybrook.cse308.gerrybackend.enums.types;

public enum PoliticalParty implements IntEnumInterface {
    DEMOCRATIC(0),
    REPUBLICAN(1),
    INDEPENDENT(2),
    OTHER(3),
    TIE(4),
    NOT_SET(5);

    public final int value;

    PoliticalParty(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }

    public static PoliticalParty getDefault(){
        return PoliticalParty.NOT_SET;
    }
}
