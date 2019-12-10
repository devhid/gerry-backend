package edu.stonybrook.cse308.gerrybackend.algorithms.workers;

import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.PhaseZeroInputs;
import edu.stonybrook.cse308.gerrybackend.algorithms.reports.PhaseZeroReport;
import edu.stonybrook.cse308.gerrybackend.data.graph.ElectionData;
import edu.stonybrook.cse308.gerrybackend.data.reports.DemoBloc;
import edu.stonybrook.cse308.gerrybackend.data.reports.PrecinctBlocSummary;
import edu.stonybrook.cse308.gerrybackend.data.reports.VoteBloc;
import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import edu.stonybrook.cse308.gerrybackend.enums.types.PoliticalParty;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;

import java.util.*;
import java.util.stream.Collectors;

public class PhaseZeroWorker extends AlgPhaseWorker<PhaseZeroInputs, PhaseZeroReport> {

    /**
     * Retrieve demographic bloc information about each precinct in a state.
     *
     * @param state     the StateNode graph
     * @param threshold the user input threshold for demographic blocs
     * @return a map whose keys are precincts who have demographic blocs and values are POJOS
     */
    private static Map<PrecinctNode, DemoBloc> getDemoBlocs(StateNode state, double threshold) {
        Map<PrecinctNode, DemoBloc> demoBlocs = new HashMap<>();
        state.getChildren().forEach(d -> {
            d.getChildren().forEach(p -> {
                int precinctTotalPop = p.getDemographicData().getTotalPopulation();
                EnumSet.allOf(DemographicType.class).forEach(demoType -> {
                    int precinctDemoPop = p.getDemographicData().getDemoPopulation(demoType);
                    if (((double) precinctDemoPop / precinctTotalPop) >= threshold) {
                        demoBlocs.put(p, new DemoBloc(demoType, precinctDemoPop, precinctTotalPop));
                    }
                });
            });
        });
        return demoBlocs;
    }

    /**
     * Retrieve vote bloc information about each precinct with a demographic bloc.
     *
     * @param demoBlocs the demographic bloc information retrieved from {@link #getDemoBlocs}
     * @param threshold the user input threshold for vote blocs
     * @return a map whose keys are precincts with demographic and vote blocs and values are POJOs
     */
    private static Map<PrecinctNode, VoteBloc> getVoteBlocs(Map<PrecinctNode, DemoBloc> demoBlocs, double threshold) {
        Map<PrecinctNode, VoteBloc> voteBlocs = new HashMap<>();
        demoBlocs.keySet().forEach(precinct -> {
            ElectionData precinctElectionData = precinct.getElectionData();
            PoliticalParty winningParty = precinctElectionData.getWinner();
            int winningVotes = precinctElectionData.getPartyVotes(winningParty);
            int totalVotes = precinctElectionData.getTotalVotes();
            if (((double) winningVotes / totalVotes) >= threshold) {
                voteBlocs.put(precinct, new VoteBloc(winningParty, winningVotes, totalVotes));
            }
        });
        return voteBlocs;
    }

    /**
     * Generates {@class PhaseZeroReport} by aggregating demographic bloc and voting bloc information.
     *
     * @param demoBlocs the demographic bloc information retrieved from {@link #getDemoBlocs}
     * @param voteBlocs the voting bloc information retrieved from {@link #getVoteBlocs}
     * @return a PhaseZeroReport object containing the finalized aggregated voter and demographic information.
     */
    private static PhaseZeroReport aggregatePhaseZeroReport(Map<PrecinctNode, DemoBloc> demoBlocs,
                                                            Map<PrecinctNode, VoteBloc> voteBlocs) {
        final Map<PoliticalParty, Map<DemographicType, PrecinctBlocSummary>> precinctBlocSummaries = new HashMap<>();

        // Iterate through all voting blocs found
        voteBlocs.forEach((precinctWithVoteBloc, voteBloc) -> {
            final DemoBloc demoBloc = demoBlocs.get(precinctWithVoteBloc);
            final DemographicType demographicType = demoBloc.getDemographicType();
            final Map<DemographicType, PrecinctBlocSummary> demographicEntry =
                    precinctBlocSummaries.getOrDefault(voteBloc.getWinningParty(), new HashMap<>());
            if (!precinctBlocSummaries.containsKey(voteBloc.getWinningParty())){
                precinctBlocSummaries.put(voteBloc.getWinningParty(), demographicEntry);
            }
            final PrecinctBlocSummary precinctBlocSummary = demographicEntry.getOrDefault(demographicType,
                    new PrecinctBlocSummary(voteBloc.getWinningParty(), demographicType));

            double votePercent = (1.0 * voteBloc.getWinningVotes()) / voteBloc.getTotalVotes();
            double demographicPercent = (1.0 * demoBloc.getDemographicPopulation()) / demoBloc.getTotalPop();

            // Update stats
            precinctBlocSummary.incrementVotingBlocCount();
            precinctBlocSummary.addPartyPercentage(votePercent);
            precinctBlocSummary.addDemographicPercentage(demographicPercent);
            precinctBlocSummary.addCounty(precinctWithVoteBloc.getCounty());

            demographicEntry.put(demographicType, precinctBlocSummary);
        });

        final Map<PoliticalParty, List<PrecinctBlocSummary>> result = precinctBlocSummaries.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, partyEntry -> new ArrayList<>(partyEntry.getValue().values())));
        return new PhaseZeroReport(result);
    }


    @Override
    public PhaseZeroReport run(PhaseZeroInputs inputs) {
        // Retrieve demographic bloc information from precincts.
        Map<PrecinctNode, DemoBloc> demoBlocs = getDemoBlocs(inputs.getState(), inputs.getPopulationThreshold());

        // For precincts with a demographic bloc, check to see if they also have vote blocs.
        Map<PrecinctNode, VoteBloc> voteBlocs = getVoteBlocs(demoBlocs, inputs.getVoteThreshold());

        return aggregatePhaseZeroReport(demoBlocs, voteBlocs);
    }

}
