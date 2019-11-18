package edu.stonybrook.cse308.gerrybackend.algorithms.workers;

import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.PhaseTwoInputs;
import edu.stonybrook.cse308.gerrybackend.algorithms.reports.PhaseTwoReport;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseTwoPrecinctMove;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;

public class PhaseTwoTreeWorker extends PhaseTwoWorker {

    @Override
    protected boolean shouldStop(PhaseTwoPrecinctMove heuristic, StateNode state) {
        return false;
    }

    @Override
    public PhaseTwoReport run(PhaseTwoInputs inputs) {
        return null;
    }

}
