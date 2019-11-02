package edu.stonybrook.cse308.gerrybackend.enums;

public enum ClusterType {
    STATE(0),
    ORIGINAL_CD(1),
    INTERIM_DISTRICT(2);

    public final int value;

    ClusterType(int value){
        this.value = value;
    }
}
