package edu.stonybrook.cse308.gerrybackend.algorithms.heuristics.phaseone;

import edu.stonybrook.cse308.gerrybackend.data.algorithm.CandidatePairs;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseOneStop;

public interface PhaseOneStopHeuristic {

    class JoinSmallest {
        private static void filterSmallestAllowedMerges(CandidatePairs pairs, int numAllowedMerges){
            // TODO: fill in
        }
    }

    static void filterLastIterationPairs(PhaseOneStop heuristic, CandidatePairs pairs, int numAllowedMerges){
        switch (heuristic){
            case JOIN_SMALLEST:
                JoinSmallest.filterSmallestAllowedMerges(pairs, numAllowedMerges);
                break;
            default:
                throw new IllegalArgumentException("Replace this string later!");
        }
    }

}
