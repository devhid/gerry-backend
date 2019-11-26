package edu.stonybrook.cse308.gerrybackend.communication;

import edu.stonybrook.cse308.gerrybackend.algorithms.reports.PhaseZeroReport;
import edu.stonybrook.cse308.gerrybackend.data.reports.DemoBloc;
import edu.stonybrook.cse308.gerrybackend.data.reports.VoteBloc;
import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import edu.stonybrook.cse308.gerrybackend.enums.types.PoliticalParty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class used to wrap precinct blocs for client side.
 * Consists of a map entry for each political party mapped to a {@class PrecinctBlocSummary} containing both
 * political and demographic information.
 */
public class PhaseZeroResult {

    @Getter
    private final Map<PoliticalParty, List<PrecinctBlocSummary>> precinctBlocs;

    public PhaseZeroResult(final Map<PoliticalParty, List<PrecinctBlocSummary>> precinctBlocs) {
        this.precinctBlocs = precinctBlocs;
    }

    /**
     * Generates {@class PhaseZeroResult} from {@class PhaseZeroReport}.
     * @param phaseZeroReport the report to aggregate data from.
     * @return a PhaseZeroResult object containing the finalized aggregated voter and demographic information.
     */
    public static PhaseZeroResult fromPhaseZeroReport(final PhaseZeroReport phaseZeroReport) {
        final Map<PoliticalParty, Map<DemographicType, PrecinctBlocSummary>> precinctBlocSummaries = new HashMap<>();

        // Iterate through voting bloc precinct
        for (String voteBlocKey : phaseZeroReport.getPrecinctVoteBlocs().keySet()) {
            // Get associated precinct data
            final DemoBloc demoBloc = phaseZeroReport.getPrecinctDemoBlocs().get(voteBlocKey);
            final VoteBloc voteBloc = phaseZeroReport.getPrecinctVoteBlocs().get(voteBlocKey);
            final Map<DemographicType, Integer> demographicMap = demoBloc.getDemoBlocPopulation();

            // Iterate over each demographic that forms a bloc in this precinct
            for (DemographicType demographicType : demographicMap.keySet()) {
                final Map<DemographicType, PrecinctBlocSummary> demographicEntry =
                        precinctBlocSummaries.getOrDefault(voteBloc.getWinningParty(), new HashMap<>());
                final PrecinctBlocSummary precinctBlocSummary = demographicEntry.getOrDefault(demographicType,
                        new PrecinctBlocSummary(voteBloc.getWinningParty(), demographicType));

                double votePercent = (1.0 * voteBloc.getWinningVotes()) / voteBloc.getTotalVotes();
                double demographicPercent = (1.0 * demographicMap.get(demographicType)) / demoBloc.getTotalPop();

                // Update stats
                precinctBlocSummary.incrementVotingBlocCount();
                precinctBlocSummary.addPartyPercentage(votePercent);
                precinctBlocSummary.addDemographicPercentage(demographicPercent);

                demographicEntry.put(demographicType, precinctBlocSummary);
            }
        }

        final Map<PoliticalParty, List<PrecinctBlocSummary>> result = precinctBlocSummaries.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> new ArrayList<>(e.getValue().values())));
        return new PhaseZeroResult(result);
    }

}
