package edu.stonybrook.cse308.gerrybackend.algorithms.measures;

import edu.stonybrook.cse308.gerrybackend.enums.measures.PopulationEqualityEnum;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;

public interface PopulationEqualityMeasure {

    class MostToLeast {
        public static double computePopulationEqualityScore(StateNode state) {
            DistrictNode mostPopulated = getMostPopulatedDistrict(state);
            DistrictNode leastPopulated = getLeastPopulatedDistrict(state);
            return computePercentDifference(mostPopulated, leastPopulated);
        }

        private static DistrictNode getMostPopulatedDistrict(StateNode state){
            // TODO: fill in
            return null;
        }

        private static DistrictNode getLeastPopulatedDistrict(StateNode state){
            // TODO: fill in
            return null;
        }

        private static double computePercentDifference(DistrictNode mostPopulated, DistrictNode leastPopulated){
            // TODO: fill in
            return -1.0;
        }
    }

    static double computePopulationEqualityScore(PopulationEqualityEnum measure, StateNode state){
        double popEqualityScore = 0.0;
        switch (measure){
            case MOST_TO_LEAST:
                popEqualityScore = MostToLeast.computePopulationEqualityScore(state);
                break;
            default:
                throw new IllegalArgumentException("Replace this string later!");
        }
        return popEqualityScore;
    }

}
