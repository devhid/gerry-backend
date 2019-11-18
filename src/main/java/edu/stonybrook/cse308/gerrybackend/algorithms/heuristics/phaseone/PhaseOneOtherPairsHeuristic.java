package edu.stonybrook.cse308.gerrybackend.algorithms.heuristics.phaseone;

import edu.stonybrook.cse308.gerrybackend.data.UnorderedPair;
import edu.stonybrook.cse308.gerrybackend.data.algorithm.LikelyCandidatePair;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseOneOtherPairs;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;

import java.util.Set;

public interface PhaseOneOtherPairsHeuristic {

    class Standard {
        public static Set<LikelyCandidatePair> determinePairs(StateNode state,
                                                              Set<LikelyCandidatePair> majMinPairs){
            // TODO: fill in
            return null;
        }
    }

    static Set<LikelyCandidatePair> determinePairs(PhaseOneOtherPairs heuristic,
                                                           StateNode state,
                                                           Set<LikelyCandidatePair> majMinPairs){
        Set<LikelyCandidatePair> pairs;
        switch (heuristic){
            case STANDARD:
                pairs = Standard.determinePairs(state, majMinPairs);
                break;
            default:
                throw new IllegalArgumentException("Replace this string later!");
        }
        return pairs;
    }
}