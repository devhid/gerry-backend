package edu.stonybrook.cse308.gerrybackend.data.measures.scores;

import edu.stonybrook.cse308.gerrybackend.enums.measures.PopulationEquality;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PopulationEqualityScore extends MeasureScore<PopulationEquality> {

    public PopulationEqualityScore(PopulationEquality measure, double score) {
        super(measure, score);
    }

}
