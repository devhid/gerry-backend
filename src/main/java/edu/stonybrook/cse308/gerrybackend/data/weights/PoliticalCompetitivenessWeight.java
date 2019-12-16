package edu.stonybrook.cse308.gerrybackend.data.weights;

import edu.stonybrook.cse308.gerrybackend.enums.measures.PoliticalCompetitiveness;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PoliticalCompetitivenessWeight extends MeasureWeight<PoliticalCompetitiveness> {

    public PoliticalCompetitivenessWeight(PoliticalCompetitiveness measure, double weight) {
        super(measure, weight);
    }

}
