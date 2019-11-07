package edu.stonybrook.cse308.gerrybackend.enums.types;

public enum NodeType implements IntEnumInterface {
    ORIGINAL(0),
    USER(1),
    NOT_SET(2);

    public final int value;

    NodeType(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }

    public static NodeType getDefault(){
        return NodeType.NOT_SET;
    }
}
