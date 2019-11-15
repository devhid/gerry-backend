package edu.stonybrook.cse308.gerrybackend.algorithms.heuristics;

import edu.stonybrook.cse308.gerrybackend.algorithms.heuristics.phaseone.pairs.PhaseOneMajorityMinorityPairsHeuristic;
import edu.stonybrook.cse308.gerrybackend.algorithms.heuristics.phaseone.pairs.PhaseOneOtherPairsHeuristic;
import edu.stonybrook.cse308.gerrybackend.algorithms.heuristics.phaseone.stop.*;
import edu.stonybrook.cse308.gerrybackend.algorithms.heuristics.phasetwo.move.PhaseTwoRandomPrecinctMove;
import edu.stonybrook.cse308.gerrybackend.data.UnorderedPair;
import edu.stonybrook.cse308.gerrybackend.data.algorithm.CandidatePairs;
import edu.stonybrook.cse308.gerrybackend.data.algorithm.PrecinctMove;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseOneMajMinPairs;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseOneOtherPairs;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseOneStop;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseTwoPrecinctMove;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class HeuristicsMapper {

    private static Map<PhaseOneMajMinPairs, Function<StateNode, Set<UnorderedPair<DistrictNode>>>> phaseOneMajMinPairsHeuristics;
    private static Map<PhaseOneOtherPairs, Function<StateNode, Set<UnorderedPair<DistrictNode>>>> phaseOneOtherPairsHeuristics;
    private static Map<PhaseOneStop, BiConsumer<CandidatePairs,Integer>> phaseOneStopHeuristics;
    private static Map<PhaseTwoPrecinctMove, Function<StateNode,PrecinctMove>> precinctMoveHeuristics;
    static {
        // Phase One Majority Minority Pairs
        phaseOneMajMinPairsHeuristics = new EnumMap<>(PhaseOneMajMinPairs.class);
        phaseOneMajMinPairsHeuristics.put(PhaseOneMajMinPairs.STANDARD, PhaseOneMajorityMinorityPairsHeuristic::determinePairs);

        // Phase One Other Pairs
        phaseOneOtherPairsHeuristics = new EnumMap<>(PhaseOneOtherPairs.class);
        phaseOneOtherPairsHeuristics.put(PhaseOneOtherPairs.STANDARD, PhaseOneOtherPairsHeuristic::determinePairs);

        // Phase One Stop
        phaseOneStopHeuristics = new EnumMap<>(PhaseOneStop.class);
        phaseOneStopHeuristics.put(PhaseOneStop.JOIN_SMALLEST, PhaseOneJoinSmallestStopHeuristic::filterLastIterationPairs);

        // Precinct Move
        precinctMoveHeuristics = new EnumMap<>(PhaseTwoPrecinctMove.class);
        precinctMoveHeuristics.put(PhaseTwoPrecinctMove.RANDOM, PhaseTwoRandomPrecinctMove::selectPrecinct);
    }

    public static Set<UnorderedPair<DistrictNode>> determinePairs(PhaseOneMajMinPairs heuristic, StateNode state){
        return phaseOneMajMinPairsHeuristics.get(heuristic).apply(state);
    }

    public static Set<UnorderedPair<DistrictNode>> determinePairs(PhaseOneOtherPairs heuristic, StateNode state){
        return phaseOneOtherPairsHeuristics.get(heuristic).apply(state);
    }

    public static void filterLastIterationPairs(PhaseOneStop heuristic, CandidatePairs pairs, int numAllowedMerges){
        phaseOneStopHeuristics.get(heuristic).accept(pairs, numAllowedMerges);
    }

    public static PrecinctMove selectPrecinct(PhaseTwoPrecinctMove heuristic, StateNode state){
        return precinctMoveHeuristics.get(heuristic).apply(state);
    }

}
