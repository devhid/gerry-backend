package edu.stonybrook.cse308.gerrybackend.algorithms.heuristics.phasetwo;

import edu.stonybrook.cse308.gerrybackend.data.algorithm.PrecinctMove;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseTwoPrecinctMove;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;

public interface PhaseTwoPrecinctMoveHeuristic {

    class Random {
        public static PrecinctMove selectPrecinct(StateNode state) {
            // TODO: fill in
            return null;
        }
    }

    static PrecinctMove selectPrecinct(PhaseTwoPrecinctMove heuristic, StateNode state){
        PrecinctMove precinctMove;
        switch (heuristic){
            case RANDOM:
                precinctMove = Random.selectPrecinct(state);
                break;
            default:
                throw new IllegalArgumentException("Replace this string later!");
        }
        return precinctMove;
    }

}