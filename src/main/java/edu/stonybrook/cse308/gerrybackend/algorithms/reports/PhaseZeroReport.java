package edu.stonybrook.cse308.gerrybackend.algorithms.reports;

import edu.stonybrook.cse308.gerrybackend.data.reports.PrecinctBlocSummary;
import edu.stonybrook.cse308.gerrybackend.enums.types.PoliticalParty;
import edu.stonybrook.cse308.gerrybackend.enums.types.StatusCode;
import lombok.Getter;

import java.util.List;
import java.util.Map;

public class PhaseZeroReport extends AlgPhaseReport {

    @Getter
    private final Map<PoliticalParty, List<PrecinctBlocSummary>> precinctBlocs;

    public PhaseZeroReport(Map<PoliticalParty, List<PrecinctBlocSummary>> precinctBlocs) {
        super(StatusCode.SUCCESS);
        this.precinctBlocs = precinctBlocs;
    }

}
