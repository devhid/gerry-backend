package edu.stonybrook.cse308.gerrybackend.enums;

public enum PoliticalParty {
    DEMOCRATIC(0),
    REPUBLICAN(1),
    INDEPENDENT(2),
    OTHER(3),
    TIE(4);

    public final int value;

    PoliticalParty(int value){
        this.value = value;
    }
}
