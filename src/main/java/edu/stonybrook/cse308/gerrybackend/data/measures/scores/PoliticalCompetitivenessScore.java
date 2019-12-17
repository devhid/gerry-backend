package edu.stonybrook.cse308.gerrybackend.data.measures.scores;

import edu.stonybrook.cse308.gerrybackend.enums.measures.PoliticalCompetitiveness;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PoliticalCompetitivenessScore extends MeasureScore<PoliticalCompetitiveness> {

    public PoliticalCompetitivenessScore(PoliticalCompetitiveness measure, double score) {
        super(measure, score);
    }

}