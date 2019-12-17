package edu.stonybrook.cse308.gerrybackend.data.measures.scores;

import edu.stonybrook.cse308.gerrybackend.enums.measures.PoliticalFairness;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PoliticalFairnessScore extends MeasureScore<PoliticalFairness> {

    public PoliticalFairnessScore(PoliticalFairness measure, double score) {
        super(measure, score);
    }

}
