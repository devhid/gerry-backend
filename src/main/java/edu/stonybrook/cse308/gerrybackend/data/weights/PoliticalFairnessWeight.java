package edu.stonybrook.cse308.gerrybackend.data.weights;

import edu.stonybrook.cse308.gerrybackend.enums.measures.PoliticalFairness;

public class PoliticalFairnessWeight extends MeasureWeight<PoliticalFairness> {

    PoliticalFairnessWeight(PoliticalFairness measure, double weight) {
        super(measure, weight);
    }

}
