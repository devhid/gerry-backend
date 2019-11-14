package edu.stonybrook.cse308.gerrybackend.algorithms.heuristics;

import edu.stonybrook.cse308.gerrybackend.algorithms.heuristics.phaseone.stop.*;
import edu.stonybrook.cse308.gerrybackend.algorithms.heuristics.phasetwo.RandomPrecinctMove;
import edu.stonybrook.cse308.gerrybackend.data.algorithm.CandidatePairs;
import edu.stonybrook.cse308.gerrybackend.data.algorithm.PrecinctMove;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseOneStopEnum;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PrecinctMoveEnum;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class HeuristicsMapper {

    private static Map<PhaseOneStopEnum, BiConsumer<CandidatePairs,Integer>> phaseOneStopHeuristics;
    private static Map<PrecinctMoveEnum, Function<StateNode,PrecinctMove>> precinctMoveHeuristics;
    static {
        // Phase One Stop
        phaseOneStopHeuristics = new EnumMap<>(PhaseOneStopEnum.class);
        phaseOneStopHeuristics.put(PhaseOneStopEnum.JOIN_SMALLEST, PhaseOneJoinSmallestStop::filterLastIterationPairs);

        // Precinct Move
        precinctMoveHeuristics = new EnumMap<>(PrecinctMoveEnum.class);
        precinctMoveHeuristics.put(PrecinctMoveEnum.RANDOM, RandomPrecinctMove::selectPrecinct);
    }

    public static void filterLastIterationPairs(PhaseOneStopEnum heuristic, CandidatePairs pairs, int numAllowedMerges){
        phaseOneStopHeuristics.get(heuristic).accept(pairs, numAllowedMerges);
    }

    public static PrecinctMove selectPrecinct(PrecinctMoveEnum heuristic, StateNode state){
        return precinctMoveHeuristics.get(heuristic).apply(state);
    }

}
