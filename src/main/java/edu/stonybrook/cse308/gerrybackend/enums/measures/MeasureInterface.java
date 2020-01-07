package edu.stonybrook.cse308.gerrybackend.enums.measures;

import com.fasterxml.jackson.annotation.JsonValue;

public interface MeasureInterface {

    @JsonValue
    String getName();

}
