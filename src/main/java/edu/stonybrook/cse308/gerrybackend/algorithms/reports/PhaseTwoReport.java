package edu.stonybrook.cse308.gerrybackend.algorithms.reports;

import edu.stonybrook.cse308.gerrybackend.data.reports.PhaseTwoMoveDelta;
import lombok.Getter;

import java.util.Queue;

public class PhaseTwoReport extends AlgPhaseReport {

    @Getter
    private Queue<PhaseTwoMoveDelta> deltas;

    public PhaseTwoReport(Queue<PhaseTwoMoveDelta> deltas) {
        this.deltas = deltas;
    }

}
