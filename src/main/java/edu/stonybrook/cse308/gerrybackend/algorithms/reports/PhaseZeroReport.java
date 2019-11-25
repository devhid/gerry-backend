package edu.stonybrook.cse308.gerrybackend.algorithms.reports;

import edu.stonybrook.cse308.gerrybackend.communication.PhaseZeroResult;
import edu.stonybrook.cse308.gerrybackend.communication.PrecinctBlocSummary;
import edu.stonybrook.cse308.gerrybackend.data.reports.DemoBloc;
import edu.stonybrook.cse308.gerrybackend.data.reports.VoteBloc;
import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import edu.stonybrook.cse308.gerrybackend.enums.types.PoliticalParty;

import java.util.HashMap;
import java.util.Map;

public class PhaseZeroReport extends AlgPhaseReport {

    private Map<String, DemoBloc> precinctDemoBlocs;
    private Map<String, VoteBloc> precinctVoteBlocs;

    public PhaseZeroReport(Map<String, DemoBloc> precinctDemoBlocs, Map<String, VoteBloc> precinctVoteBlocs) {
        this.precinctDemoBlocs = precinctDemoBlocs;
        this.precinctVoteBlocs = precinctVoteBlocs;
    }

    private Map<PoliticalParty, Map<DemographicType, PrecinctBlocSummary>> getVoteBlocSummaries() {
        final Map<PoliticalParty, Map<DemographicType, PrecinctBlocSummary>> precinctBlocSummaries = new HashMap<>();
        for (PoliticalParty party : PoliticalParty.values()) {
            precinctBlocSummaries.put(party, new HashMap<>());
        }

        for (String voteBlocKey : this.precinctVoteBlocs.keySet()) {
            final DemoBloc demoBloc = this.precinctDemoBlocs.get(voteBlocKey);
            final VoteBloc voteBloc = this.precinctVoteBlocs.get(voteBlocKey);
            final Map<DemographicType, Integer> demographicMap = demoBloc.getDemoBlocPopulation();

            for (DemographicType demographicType : demographicMap.keySet()) {
                final Map<DemographicType, PrecinctBlocSummary> votingBlocs =
                        precinctBlocSummaries.get(voteBloc.getWinningParty());
                final PrecinctBlocSummary precinctBlocSummary = votingBlocs.getOrDefault(demographicType,
                        new PrecinctBlocSummary(voteBloc.getWinningParty(), demographicType));

                precinctBlocSummary.incrementVotingBlocCount();
                double votePercent = (1.0 * voteBloc.getWinningVotes()) / voteBloc.getTotalVotes();
                double demographicPercent = (1.0 * demographicMap.get(demographicType)) / demoBloc.getTotalPop();

                precinctBlocSummary.addPartyPercentage(votePercent);
                precinctBlocSummary.addDemographicPercentage(demographicPercent);
                votingBlocs.put(demographicType, precinctBlocSummary);
            }
        }

        return precinctBlocSummaries;
    }

    public PhaseZeroResult getPhaseZeroResults() {
        return new PhaseZeroResult(this.getVoteBlocSummaries());
    }

}
