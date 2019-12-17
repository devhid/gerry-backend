package edu.stonybrook.cse308.gerrybackend.data.measures.weights;

import edu.stonybrook.cse308.gerrybackend.enums.measures.Compactness;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CompactnessWeight extends MeasureWeight<Compactness> {

    public CompactnessWeight(Compactness measure, double weight) {
        super(measure, weight);
    }

}
