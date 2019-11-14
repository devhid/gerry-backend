package edu.stonybrook.cse308.gerrybackend.algorithms.workers;

import edu.stonybrook.cse308.gerrybackend.algorithms.heuristics.HeuristicsMapper;
import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.AlgPhaseInputs;
import edu.stonybrook.cse308.gerrybackend.algorithms.reports.PhaseTwoReport;
import edu.stonybrook.cse308.gerrybackend.data.algorithm.PrecinctMove;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PrecinctMoveEnum;
import edu.stonybrook.cse308.gerrybackend.enums.types.PhaseTwoType;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;

public class PhaseTwoWorker extends AlgPhaseWorker<PhaseTwoReport> {

    // TODO: remove
    private PhaseTwoType phaseTwoType;

    private double computeObjectiveFunction(){
        // TODO: fill in
        return -1.0;
    }

    private PrecinctMove selectPrecinctMove(PrecinctMoveEnum heuristic, StateNode state){
        return HeuristicsMapper.selectPrecinct(heuristic, state);
    }

    private boolean shouldStop(StateNode state, PrecinctMoveEnum heuristic){
        // TODO: fill in
        return true;
    }

    @Override
    public PhaseTwoReport run(AlgPhaseInputs inputs) {
        return null;
    }
}
