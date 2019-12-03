package edu.stonybrook.cse308.gerrybackend.data.weights;

import edu.stonybrook.cse308.gerrybackend.enums.measures.PopulationEquality;

public class PopulationEqualityWeight extends MeasureWeight<PopulationEquality> {

    PopulationEqualityWeight(PopulationEquality measure, double weight) {
        super(measure, weight);
    }

}
