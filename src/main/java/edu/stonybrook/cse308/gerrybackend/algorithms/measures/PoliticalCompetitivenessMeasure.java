package edu.stonybrook.cse308.gerrybackend.algorithms.measures;

import edu.stonybrook.cse308.gerrybackend.enums.measures.PoliticalCompetitivenessEnum;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;

public interface PoliticalCompetitivenessMeasure {

    class MarginOfVictory {
        public static double computeCompetitivenessScore(DistrictNode district){
            return -1.0;
        }
    }

    static double computeCompetitivenessScore(PoliticalCompetitivenessEnum measure, DistrictNode district){
        double competitivenessScore = 0.0;
        switch (measure){
            case MARGIN_OF_VICTORY:
                competitivenessScore = MarginOfVictory.computeCompetitivenessScore(district);
                break;
            default:
                throw new IllegalArgumentException("Replace this string later!");
        }
        return competitivenessScore;
    }
    
}
