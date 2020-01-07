package edu.stonybrook.cse308.gerrybackend.data.measures.scores;

import edu.stonybrook.cse308.gerrybackend.enums.measures.Compactness;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CompactnessScore extends MeasureScore<Compactness> {

    public CompactnessScore(Compactness measure, double score) {
        super(measure, score);
    }

}
