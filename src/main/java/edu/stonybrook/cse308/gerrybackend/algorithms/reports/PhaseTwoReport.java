package edu.stonybrook.cse308.gerrybackend.algorithms.reports;

import edu.stonybrook.cse308.gerrybackend.data.reports.PhaseTwoMoveDelta;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import lombok.Getter;

import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class PhaseTwoReport extends AlgPhaseReport {

    @Getter
    private Queue<PhaseTwoMoveDelta> deltas;

    public PhaseTwoReport(Queue<PhaseTwoMoveDelta> deltas) {
        this.deltas = deltas;
    }

}
