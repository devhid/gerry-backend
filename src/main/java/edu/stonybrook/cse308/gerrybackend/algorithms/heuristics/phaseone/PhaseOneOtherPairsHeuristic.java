package edu.stonybrook.cse308.gerrybackend.algorithms.heuristics.phaseone;

import edu.stonybrook.cse308.gerrybackend.data.UnorderedPair;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseOneOtherPairs;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;

import java.util.Set;

public interface PhaseOneOtherPairsHeuristic {

    class Standard {
        public static Set<UnorderedPair<DistrictNode>> determinePairs(StateNode state){
            // TODO: fill in
            return null;
        }
    }

    static Set<UnorderedPair<DistrictNode>> determinePairs(PhaseOneOtherPairs heuristic, StateNode state){
        Set<UnorderedPair<DistrictNode>> pairs;
        switch (heuristic){
            case STANDARD:
                pairs = Standard.determinePairs(state);
                break;
            default:
                throw new IllegalArgumentException("Replace this string later!");
        }
        return pairs;
    }
}