package edu.stonybrook.cse308.gerrybackend.enums.measures;

import com.fasterxml.jackson.annotation.JsonValue;
import edu.stonybrook.cse308.gerrybackend.enums.IntEnumInterface;
import lombok.Getter;

public enum Measures implements IntEnumInterface {
    COMPACTNESS(0, "compactness"),
    POLITICAL_COMPETITIVENESS(1, "political_competitiveness"),
    POLITICAL_FAIRNESS(2, "political_fairness"),
    POPULATION_EQUALITY(3, "population_equality"),
    POPULATION_HOMOGENEITY(4, "population_homogeneity");

    @Getter
    private final int value;
    private final String name;

    Measures(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return this.name;
    }

}
