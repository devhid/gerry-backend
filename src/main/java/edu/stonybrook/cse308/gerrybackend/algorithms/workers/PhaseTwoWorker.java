package edu.stonybrook.cse308.gerrybackend.algorithms.workers;

import edu.stonybrook.cse308.gerrybackend.algorithms.heuristics.Heuristics;
import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.PhaseTwoInputs;
import edu.stonybrook.cse308.gerrybackend.algorithms.reports.PhaseTwoReport;
import edu.stonybrook.cse308.gerrybackend.data.algorithm.PrecinctMove;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseTwoPrecinctMove;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;

import java.util.Map;

public abstract class PhaseTwoWorker extends AlgPhaseWorker<PhaseTwoInputs, PhaseTwoReport> {

    protected Map<PrecinctMove, StateNode> potentialMoves;

    protected double computeObjectiveFunction() {
        // TODO: fill in
        return -1.0;
    }

    protected PrecinctMove selectPrecinctMove(PhaseTwoPrecinctMove heuristic, StateNode state) {
        return Heuristics.selectPrecinct(heuristic, state);
    }

    protected abstract boolean shouldStop(PhaseTwoPrecinctMove heuristic, StateNode state);

    public abstract PhaseTwoReport run(PhaseTwoInputs inputs);

}
