package edu.stonybrook.cse308.gerrybackend.algorithms.measures.population.equality;

import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;

public class MostToLeastPopulationEquality {

    public static double computePopulationEqualityScore(StateNode state){
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
