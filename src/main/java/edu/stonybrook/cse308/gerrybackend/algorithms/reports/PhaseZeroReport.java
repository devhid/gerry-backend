package edu.stonybrook.cse308.gerrybackend.algorithms.reports;

import edu.stonybrook.cse308.gerrybackend.data.reports.DemoBloc;
import edu.stonybrook.cse308.gerrybackend.data.reports.VoteBloc;

import java.util.Map;

public class PhaseZeroReport extends AlgPhaseReport {

    private Map<String,DemoBloc> precinctDemoBlocs;
    private Map<String,VoteBloc> precinctVoteBlocs;

    public PhaseZeroReport(Map<String,DemoBloc> precinctDemoBlocs, Map<String,VoteBloc> precinctVoteBlocs) {
        this.precinctDemoBlocs = precinctDemoBlocs;
        this.precinctVoteBlocs = precinctVoteBlocs;
    }

}
