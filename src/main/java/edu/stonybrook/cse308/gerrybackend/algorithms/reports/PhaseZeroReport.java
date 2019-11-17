package edu.stonybrook.cse308.gerrybackend.algorithms.reports;

import edu.stonybrook.cse308.gerrybackend.data.reports.DemoBloc;
import edu.stonybrook.cse308.gerrybackend.controllers.mappings.PhaseZeroResult;
import edu.stonybrook.cse308.gerrybackend.data.reports.VoteBloc;
import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import edu.stonybrook.cse308.gerrybackend.enums.types.PoliticalParty;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PhaseZeroReport extends AlgPhaseReport {

    private Map<String,DemoBloc> precinctDemoBlocs;
    private Map<String,VoteBloc> precinctVoteBlocs;

    public PhaseZeroReport(Map<String,DemoBloc> precinctDemoBlocs, Map<String,VoteBloc> precinctVoteBlocs) {
        this.precinctDemoBlocs = precinctDemoBlocs;
        this.precinctVoteBlocs = precinctVoteBlocs;
    }

    public Set<PhaseZeroResult> getPhaseZeroResults() {
        final Set<PhaseZeroResult> phaseZeroResults = new HashSet<>();
        for (Map.Entry<String, VoteBloc> voteBlocEntry : this.precinctVoteBlocs.entrySet()) {
            final Map<DemographicType, Integer> demographicMap = this.precinctDemoBlocs.get(voteBlocEntry.getKey())
                                                                        .getDemoBlocPopulation();
            final String precinctId = voteBlocEntry.getKey();
            final PoliticalParty politicalParty = voteBlocEntry.getValue().getWinningParty();
            final int partyVote = voteBlocEntry.getValue().getWinningVotes();
            final int totalVote = voteBlocEntry.getValue().getTotalVotes();
            final int totalPopulation = this.precinctDemoBlocs.get(voteBlocEntry.getKey()).getTotalPop();
            for (Map.Entry<DemographicType, Integer> demographic : demographicMap.entrySet()) {
                phaseZeroResults.add(new PhaseZeroResult(precinctId, demographic, totalPopulation, politicalParty,
                        partyVote, totalVote));
            }
        }
        return phaseZeroResults;
    }

}
