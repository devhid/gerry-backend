package edu.stonybrook.cse308.gerrybackend.algorithms.measures;

import edu.stonybrook.cse308.gerrybackend.enums.measures.PoliticalCompetitivenessEnum;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;

public interface PoliticalCompetitivenessMeasure {

    class MarginOfVictory {
        public static double computeCompetitivenessScore(StateNode state){
            return -1.0;
        }
    }

    static double computeCompetitivenessScore(PoliticalCompetitivenessEnum measure, StateNode state){
        double competitivenessScore = 0.0;
        switch (measure){
            case MARGIN_OF_VICTORY:
                competitivenessScore = MarginOfVictory.computeCompetitivenessScore(state);
                break;
            default:
                throw new IllegalArgumentException("Replace this string later!");
        }
        return competitivenessScore;
    }
    
}
