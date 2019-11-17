package edu.stonybrook.cse308.gerrybackend.algorithms.workers;

import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.PhaseZeroInputs;
import edu.stonybrook.cse308.gerrybackend.algorithms.reports.PhaseZeroReport;
import edu.stonybrook.cse308.gerrybackend.data.graph.ElectionData;
import edu.stonybrook.cse308.gerrybackend.data.reports.DemoBloc;
import edu.stonybrook.cse308.gerrybackend.data.reports.VoteBloc;
import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import edu.stonybrook.cse308.gerrybackend.enums.types.PoliticalParty;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;

import java.util.*;
import java.util.stream.Collectors;

public class PhaseZeroWorker extends AlgPhaseWorker<PhaseZeroInputs, PhaseZeroReport> {

    private Map<PrecinctNode,DemoBloc> getDemoBlocs(StateNode state, double threshold){
        Set<PrecinctNode> allPrecincts = state.getAllPrecincts();
        Map<PrecinctNode,DemoBloc> demoBlocs = new HashMap<>();
        allPrecincts.forEach(p -> {
            int precinctTotalPop = p.getDemographicData().getDemoPopulation(DemographicType.ALL);
            DemoBloc precinctDemoBloc = new DemoBloc(precinctTotalPop);
            EnumSet.allOf(DemographicType.class).forEach(demoType -> {
                int precinctDemoPop = p.getDemographicData().getDemoPopulation(demoType);
                if (((double)precinctDemoPop / precinctTotalPop) >= threshold){
                    precinctDemoBloc.setDemoBlocPopulation(demoType, precinctDemoPop);
                }
            });
            if (precinctDemoBloc.size() > 0){
                demoBlocs.put(p, precinctDemoBloc);
            }
        });
        return demoBlocs;
    }

    private Map<PrecinctNode,VoteBloc> getVoteBlocs(Map<PrecinctNode,DemoBloc> demoBlocs, double threshold){
        Map<PrecinctNode,VoteBloc> voteBlocs = new HashMap<>();
        demoBlocs.keySet().forEach(precinct -> {
            ElectionData precinctElectionData = precinct.getElectionData();
            PoliticalParty winningParty = precinctElectionData.getWinner();
            int winningVotes = precinctElectionData.getPartyVotes(winningParty);
            int totalVotes = precinctElectionData.getTotalVotes();
            if (((double) winningVotes / totalVotes) >= threshold){
                voteBlocs.put(precinct, new VoteBloc(winningParty, winningVotes, totalVotes));
            }
        });
        return voteBlocs;
    }

    @Override
    public PhaseZeroReport run(PhaseZeroInputs inputs) {
        Map<PrecinctNode,DemoBloc> demoBlocs = getDemoBlocs(inputs.getState(), inputs.getPopulationThreshold());
        Map<PrecinctNode,VoteBloc> voteBlocs = getVoteBlocs(demoBlocs, inputs.getVoteThreshold());

        Map<String,DemoBloc> demoBlocsTransformed = demoBlocs.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().getId(), Map.Entry::getValue));
        Map<String,VoteBloc> voteBlocsTransformed = voteBlocs.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().getId(), Map.Entry::getValue));
        return new PhaseZeroReport(demoBlocsTransformed, voteBlocsTransformed);
    }

}
