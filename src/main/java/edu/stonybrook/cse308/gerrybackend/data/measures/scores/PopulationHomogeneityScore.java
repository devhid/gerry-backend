package edu.stonybrook.cse308.gerrybackend.data.measures.scores;

import edu.stonybrook.cse308.gerrybackend.enums.measures.PopulationHomogeneity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PopulationHomogeneityScore extends MeasureScore<PopulationHomogeneity> {

    public PopulationHomogeneityScore(PopulationHomogeneity measure, double score) {
        super(measure, score);
    }

}
