package edu.stonybrook.cse308.gerrybackend.algorithms.heuristics.phaseone;

import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.PhaseOneInputs;
import edu.stonybrook.cse308.gerrybackend.data.algorithm.LikelyCandidatePair;

import java.util.Set;

public interface PhaseOneOtherPairsHeuristic {

    class Standard {
        public static Set<LikelyCandidatePair> determinePairs(PhaseOneInputs inputs,
                                                              Set<LikelyCandidatePair> majMinPairs) {
            // TODO: fill in
            return null;
        }
    }

    static Set<LikelyCandidatePair> determinePairs(PhaseOneInputs inputs,
                                                   Set<LikelyCandidatePair> majMinPairs) {
        Set<LikelyCandidatePair> pairs;
        switch (inputs.getPhaseOneOtherPairsHeuristic()) {
            case STANDARD:
                pairs = Standard.determinePairs(inputs, majMinPairs);
                break;
            default:
                throw new IllegalArgumentException("Replace this string later!");
        }
        return pairs;
    }
}