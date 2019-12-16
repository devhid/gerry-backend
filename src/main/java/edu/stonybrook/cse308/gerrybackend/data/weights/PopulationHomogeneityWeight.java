package edu.stonybrook.cse308.gerrybackend.data.weights;

import edu.stonybrook.cse308.gerrybackend.enums.measures.PopulationHomogeneity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PopulationHomogeneityWeight extends MeasureWeight<PopulationHomogeneity> {

    PopulationHomogeneityWeight(PopulationHomogeneity measure, double weight) {
        super(measure, weight);
    }

}
