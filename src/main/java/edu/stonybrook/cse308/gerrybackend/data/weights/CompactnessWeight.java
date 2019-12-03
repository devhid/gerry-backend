package edu.stonybrook.cse308.gerrybackend.data.weights;

import edu.stonybrook.cse308.gerrybackend.enums.measures.Compactness;

public class CompactnessWeight extends MeasureWeight<Compactness> {

    public CompactnessWeight(Compactness measure, double weight) {
        super(measure, weight);
    }

}
