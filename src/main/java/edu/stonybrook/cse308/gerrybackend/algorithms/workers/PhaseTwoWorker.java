package edu.stonybrook.cse308.gerrybackend.algorithms.workers;

import edu.stonybrook.cse308.gerrybackend.algorithms.heuristics.HeuristicsMapper;
import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.PhaseTwoInputs;
import edu.stonybrook.cse308.gerrybackend.algorithms.reports.PhaseTwoReport;
import edu.stonybrook.cse308.gerrybackend.data.algorithm.PrecinctMove;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseTwoPrecinctMove;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseTwoDepth;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;

public class PhaseTwoWorker extends AlgPhaseWorker<PhaseTwoInputs,PhaseTwoReport> {

    // TODO: remove
    private PhaseTwoDepth phaseTwoDepth;

    private double computeObjectiveFunction(){
        // TODO: fill in
        return -1.0;
    }

    private PrecinctMove selectPrecinctMove(PhaseTwoPrecinctMove heuristic, StateNode state){
        return HeuristicsMapper.selectPrecinct(heuristic, state);
    }

    private boolean shouldStop(StateNode state, PhaseTwoPrecinctMove heuristic){
        // TODO: fill in
        return true;
    }

    @Override
    public PhaseTwoReport run(PhaseTwoInputs inputs) {
        return null;
    }
}
