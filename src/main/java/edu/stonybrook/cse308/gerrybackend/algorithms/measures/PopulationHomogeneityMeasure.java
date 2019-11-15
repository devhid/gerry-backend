package edu.stonybrook.cse308.gerrybackend.algorithms.measures;

import edu.stonybrook.cse308.gerrybackend.enums.measures.PopulationHomogeneityEnum;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;

public interface PopulationHomogeneityMeasure {

    class NormalizedSquareError {
        public static double computePopulationHomogeneityScore(StateNode state) {
            // TODO: fill in
            return -1.0;
        }
    }

    static double computePopulationHomogeneityScore(PopulationHomogeneityEnum measure, StateNode state){
        double popHomogeneityScore = 0.0;
        switch (measure){
            case NORMALIZED_SQUARE_ERROR:
                popHomogeneityScore = NormalizedSquareError.computePopulationHomogeneityScore(state);
                break;
            default:
                throw new IllegalArgumentException("Replace this string later!");
        }
        return popHomogeneityScore;
    }

}