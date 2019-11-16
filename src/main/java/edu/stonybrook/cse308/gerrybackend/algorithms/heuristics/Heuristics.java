package edu.stonybrook.cse308.gerrybackend.algorithms.heuristics;

import edu.stonybrook.cse308.gerrybackend.algorithms.heuristics.phaseone.PhaseOneMajorityMinorityPairsHeuristic;
import edu.stonybrook.cse308.gerrybackend.algorithms.heuristics.phaseone.PhaseOneOtherPairsHeuristic;
import edu.stonybrook.cse308.gerrybackend.algorithms.heuristics.phaseone.PhaseOneStopHeuristic;
import edu.stonybrook.cse308.gerrybackend.algorithms.heuristics.phasetwo.PhaseTwoPrecinctMoveHeuristic;
import edu.stonybrook.cse308.gerrybackend.data.UnorderedPair;
import edu.stonybrook.cse308.gerrybackend.data.algorithm.CandidatePairs;
import edu.stonybrook.cse308.gerrybackend.data.algorithm.PrecinctMove;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseOneMajMinPairs;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseOneOtherPairs;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseOneStop;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseTwoPrecinctMove;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;

import java.util.Set;

public abstract class Heuristics {

    public static Set<UnorderedPair<DistrictNode>> determinePairs(PhaseOneMajMinPairs heuristic, StateNode state){
        return PhaseOneMajorityMinorityPairsHeuristic.determinePairs(heuristic, state);
    }

    public static Set<UnorderedPair<DistrictNode>> determinePairs(PhaseOneOtherPairs heuristic, StateNode state){
        return PhaseOneOtherPairsHeuristic.determinePairs(heuristic, state);
    }

    public static void filterLastIterationPairs(PhaseOneStop heuristic, CandidatePairs pairs, int numAllowedMerges){
        PhaseOneStopHeuristic.filterLastIterationPairs(heuristic, pairs, numAllowedMerges);
    }

    public static PrecinctMove selectPrecinct(PhaseTwoPrecinctMove heuristic, StateNode state){
        return PhaseTwoPrecinctMoveHeuristic.selectPrecinct(heuristic, state);
    }

}
