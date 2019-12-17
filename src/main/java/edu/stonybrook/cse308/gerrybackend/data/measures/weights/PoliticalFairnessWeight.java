package edu.stonybrook.cse308.gerrybackend.data.measures.weights;

import edu.stonybrook.cse308.gerrybackend.enums.measures.PoliticalFairness;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PoliticalFairnessWeight extends MeasureWeight<PoliticalFairness> {

    PoliticalFairnessWeight(PoliticalFairness measure, double weight) {
        super(measure, weight);
    }

}
