package edu.stonybrook.cse308.gerrybackend.algorithms.reports;

import edu.stonybrook.cse308.gerrybackend.data.reports.DemoBloc;
import edu.stonybrook.cse308.gerrybackend.data.reports.VoteBloc;
import lombok.Getter;

import java.util.Map;

public class PhaseZeroReport extends AlgPhaseReport {

    @Getter
    private Map<String, DemoBloc> precinctDemoBlocs;
    @Getter
    private Map<String, VoteBloc> precinctVoteBlocs;

    public PhaseZeroReport(Map<String, DemoBloc> precinctDemoBlocs, Map<String, VoteBloc> precinctVoteBlocs) {
        this.precinctDemoBlocs = precinctDemoBlocs;
        this.precinctVoteBlocs = precinctVoteBlocs;
    }
}
