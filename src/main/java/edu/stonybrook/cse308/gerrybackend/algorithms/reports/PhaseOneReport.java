package edu.stonybrook.cse308.gerrybackend.algorithms.reports;

import edu.stonybrook.cse308.gerrybackend.data.reports.PhaseOneMergeDelta;

import java.util.List;

public class PhaseOneReport extends AlgPhaseReport {

    private List<PhaseOneMergeDelta> deltas;

    public PhaseOneReport(List<PhaseOneMergeDelta> deltas) {
        this.deltas = deltas;
    }
}
